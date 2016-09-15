package com.cbinfo.service;

import com.cbinfo.dto.UserDataDTO;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Igor on 29.06.2016.
 */

@Service
public class UserDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataService.class);
    private static final String LOCALHOST = "localhost";
    public static final String LOCALIP = "127.0.0.1";
    private static final String SERVICE_URL_ADDRESS = "http://www.ipinfo.io/";

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


    public UserDataDTO getData(HttpServletRequest request) {
        UserDataDTO userDataDTO = new UserDataDTO();
        try {
            String ip = request.getRemoteAddr();
            userDataDTO.setCountry(countryCache.get(ip));
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage());
        }
        return setUserDataDTOFields(userDataDTO, request);
    }

    protected UserDataDTO setUserDataDTOFields(UserDataDTO userDataDTO, HttpServletRequest request) {
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        String userAgent = request.getHeader("User-Agent");
        if (isBlank(userAgent)) {
            return userDataDTO;
        }
        ReadableUserAgent agent = parser.parse(userAgent);
        userDataDTO.setUserAgent(userAgent);
        userDataDTO.setDeviceCategory(agent.getDeviceCategory().getName());
        userDataDTO.setAgentFamily(agent.getFamily().getName());
        userDataDTO.setProducer(agent.getProducer());
        userDataDTO.setBrowser(agent.getName());
        userDataDTO.setOperatingSystem(agent.getOperatingSystem().getName());
        return userDataDTO;
    }

    protected String getCountry(String ip) throws UnknownHostException {
        if (checkLocalHost(ip)) {
            // TODO make string constant
            return LOCALHOST;
        }
        // TODO fix typo
        String URLAddress = SERVICE_URL_ADDRESS + ip + "/json";
        // TODO we dont need to instantiate it with null (for local variables)
        String data;
        try {
            data = readDataFromURL(new URLWrapper(URLAddress));
        } catch (IOException e) {
            // TODO never do this - e.printStackTrace();
            LOGGER.error(e.getMessage());
            return "undefined (no connection to the service)";
        }
        // TODO move to separate method
        return getCountryFromJSON(data);
    }

    protected String getCountryFromJSON(String data) {
        Gson gson = new GsonBuilder().create();
        UserDataDTO userDataDTOForJson = gson.fromJson(data, UserDataDTO.class);
        //may be inner class than UserDataDTO?
        return userDataDTOForJson.getCountry();
    }

    protected boolean checkLocalHost(String ip) throws UnknownHostException {
        // TODO replace with ternary operator(? 2:0)
        return (ip.equals(LOCALIP)) ? true : false;
    }

    protected String readDataFromURL(URLWrapper ipInfoURL) throws IOException {
        URLConnection urlConnection = ipInfoURL.openConnection();
        InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
        return readDataFromBufferedReader(inputStreamReader);
    }

    protected String readDataFromBufferedReader(InputStreamReader inputStreamReader) {
        // TODO replace with try-with-resources(try())
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
            String input;
            StringBuilder result = new StringBuilder();
            while ((input = bufferedReader.readLine()) != null) {
                result.append(input);
            }
            return result.toString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return null;
    }

    public class URLWrapper {
        URL url;

        public URLWrapper(String spec) throws MalformedURLException {
            url = new URL(spec);
        }

        public URLConnection openConnection() throws IOException {
            return url.openConnection();
        }
    }
}
