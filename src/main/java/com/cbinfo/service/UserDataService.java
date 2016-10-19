package com.cbinfo.service;

import com.cbinfo.dto.UserDataDTO;
import com.cbinfo.model.User;
import com.cbinfo.model.UserData;
import com.cbinfo.repository.UserDataRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class UserDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataService.class);

    private static final String LOCALHOST = "localhost";
    private static final String LOCAL_IP = "127.0.0.1";
    private static final String UNDEFINED_COUNTRY = "undefined";
    private static final String IPINFO_SERVICE_URL_ADDRESS = "http://www.ipinfo.io/";
    private static final String LOCAL_IP2 = "0:0:0:0:0:0:0:1";

    LoadingCache<String, String> countryCache =
            CacheBuilder.newBuilder()
                    .maximumSize(100)
                    .expireAfterAccess(10, TimeUnit.MINUTES)
                    .build(new CacheLoader<String, String>() {

                        @Override
                        public String load(String ip) throws IOException {
                            return getCountry(ip);
                        }
                    });

    @Autowired
    protected UserDataRepository userDataRepository;

    @Autowired
    private UserService userService;

    public UserDataDTO getDataAndSave(HttpServletRequest request) {
        UserDataDTO userDataDTO = new UserDataDTO();
        setUserDataDTOCountry(userDataDTO, request);
        setUserDataDTOFields(userDataDTO, request);
        saveUserData(userDataDTO);
        return userDataDTO;
    }

    protected void setUserDataDTOCountry(UserDataDTO userDataDTO, HttpServletRequest request) {
        try {
            String ip = request.getRemoteAddr();
            userDataDTO.setCountry(countryCache.get(ip));
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage());
            userDataDTO.setCountry(UNDEFINED_COUNTRY);
        }
    }

    protected void setUserDataDTOFields(UserDataDTO userDataDTO, HttpServletRequest request) {
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        String userAgent = request.getHeader("User-Agent");
        if (isBlank(userAgent)) {
            return;
        }
        ReadableUserAgent agent = parser.parse(userAgent);
        userDataDTO.setIp(request.getRemoteAddr());
        userDataDTO.setUserAgent(userAgent);
        userDataDTO.setDeviceCategory(agent.getDeviceCategory().getName());
        userDataDTO.setAgentFamily(agent.getFamily().getName());
        userDataDTO.setProducer(agent.getProducer());
        userDataDTO.setBrowser(agent.getName());
        userDataDTO.setOperatingSystem(agent.getOperatingSystem().getName());
    }

    protected String getCountry(String ip) throws UnknownHostException {
        if (checkLocalHost(ip)) {
            return LOCALHOST;
        }
        String URLAddress = getServiceURL(ip);
        String data;
        try {
            InputStream inputStream = new URL(URLAddress).openStream();
            data = readStringFromInputStream(inputStream);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return UNDEFINED_COUNTRY;
        }
        return getCountryFromJSON(data);
    }

    protected String getServiceURL(String ip) {
        return IPINFO_SERVICE_URL_ADDRESS + ip + "/json";
    }

    protected boolean checkLocalHost(String ip) throws UnknownHostException {
        return ip.equals(LOCAL_IP)||ip.equals(LOCAL_IP2);
    }

    protected String getCountryFromJSON(String data) {
        Gson gson = new GsonBuilder().create();
        UserDataDTO userDataDTOForJson = gson.fromJson(data, UserDataDTO.class);
        return userDataDTOForJson.getCountry();
    }

    protected String readStringFromInputStream(InputStream inputStream) throws IOException {
        String result;
        try {
            result = IOUtils.toString(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<UserData> findAll() {
        return (List<UserData>) userDataRepository.findAll();
    }

    @Transactional
    public void saveUserData(UserDataDTO userDataDTO) {
        UserData userData = this.createUserData(userDataDTO);
        userDataRepository.save(userData);
    }

    protected UserData createUserData(UserDataDTO userDataDTO) {
        UserData userData = new UserData();
        userData.setTime(new Date());
        userData.setAgentFamily(userDataDTO.getAgentFamily());
        userData.setBrowser(userDataDTO.getBrowser());
        userData.setDeviceCategory(userDataDTO.getDeviceCategory());
        userData.setCountry(userDataDTO.getCountry());
        userData.setIp(userDataDTO.getIp());
        userData.setOperatingSystem(userDataDTO.getOperatingSystem());
        userData.setProducer(userDataDTO.getProducer());
        User userWithSameIp = getFirstUserWithSameIp(userDataDTO);
        userData.setUser(userWithSameIp);
        return userData;
    }

    protected User getFirstUserWithSameIp(UserDataDTO userDataDTO) {
        return (userService.findByUserIp(userDataDTO.getIp()).size() >= 1) ? userService.findByUserIp(userDataDTO.getIp()).get(0) : null;
    }
}
