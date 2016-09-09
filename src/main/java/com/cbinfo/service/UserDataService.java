package com.cbinfo.service;

import com.cbinfo.dto.UserDataDto;
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


    public UserDataDto getData(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        UserDataDto userDataDto = new UserDataDto();
        try {
            userDataDto.setCountry(countryCache.get(ip));
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage());
        }
        return setUserDataDTOFields(userDataDto, request);
    }

    protected UserDataDto setUserDataDTOFields(UserDataDto userDataDto, HttpServletRequest request) {
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        String userAgent = request.getHeader("User-Agent");
        if (isBlank(userAgent)) {
            return userDataDto;
        }
        // TODO: 06.09.16 and more agent information fields
        ReadableUserAgent agent = parser.parse(userAgent);
        userDataDto.setUserAgent(userAgent);
        userDataDto.setDeviceCategory(agent.getDeviceCategory().getName());
        userDataDto.setAgentFamily(agent.getFamily().getName());
        userDataDto.setProducer(agent.getProducer());
        userDataDto.setBrowser(agent.getName());
        userDataDto.setOperatingSystem(agent.getOperatingSystem().getName());
        return userDataDto;
    }

    protected String getCountry(String ip) throws UnknownHostException {
        if (checkLocalHost(ip)) {
            // TODO replace string with constant
            return "localhost";
        }
        // TODO fix typo
        String URLAdress = SERVICE_URL_ADDRESS + ip + "/json";
        // TODO we dont need to instantiate it with null (for local variables)
        String data = null;
        try {
            data = readDataFromURL(new URLWrapper(URLAdress));
        } catch (IOException e) {
            // TODO never do this - e.printStackTrace();
            e.printStackTrace();
            return "undefined (no connection to the service)";
        }
        // TODO move to separate method
        Gson gson = new GsonBuilder().create();
        UserDataDto userDataDtoForJson = gson.fromJson(data, UserDataDto.class);
        return userDataDtoForJson.getCountry();
    }

    protected boolean checkLocalHost(String ip) throws UnknownHostException {
        // TODO move to constants
        String localIp = "127.0.0.1";

        // TODO replace with ternary operator
        if (ip.equals(localIp)) {
            return true;
        }
        return false;
    }

    protected String readDataFromURL(URLWrapper ipInfoURL) throws IOException {
        URLConnection urlConnection = ipInfoURL.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        return readDataFromBufferedReader(bufferedReader);
    }

    protected String readDataFromBufferedReader(BufferedReader bufferedReader) {
        // TODO replace with try-with-resources
        try {
            String input;
            StringBuilder result = new StringBuilder();
            while ((input = bufferedReader.readLine()) != null) {
                result.append(input);
            }
            return result.toString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
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
