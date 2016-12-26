package com.cbinfo.service;

import com.cbinfo.dto.form.BannerForm;
import com.cbinfo.model.Banner;
import com.cbinfo.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private FlightService flightService;

    public void createBanner(BannerForm bannerForm) throws Exception {
        checkArgument(isNotBlank(bannerForm.getTitle()), "Empty title!!!");
        checkArgument(ifTitleNotExists(bannerForm), "Banner with such title already exists!");
        checkArgument(checkUrlIfCorrect(bannerForm), "You don't have such site!!!");
        checkArgument(bannerForm.getFile() != null, "Empty file!!!");
        checkArgument(ifImage(bannerForm.getFile()), "File is not image!");
        save(bannerFormToBannerOnCreate(bannerForm));
    }

    private boolean ifTitleNotExists(BannerForm bannerForm) {
        return !flightService.findFlight(bannerForm.getFlightId()).getBanners().stream().map(banner -> banner.getTitle()).anyMatch(t -> bannerForm.getTitle().equals(t));

    }

    private boolean ifImage(MultipartFile file) {
        return file.getContentType().startsWith("image");
    }

    @Transactional
    private void save(Banner banner) {
        bannerRepository.save(banner);
    }

    private Banner bannerFormToBannerOnCreate(BannerForm bannerForm) throws IOException {
        Banner result = new Banner();
        fillBanner(result, bannerForm);
        return result;
    }

    private void fillBanner(Banner banner, BannerForm bannerForm) throws IOException {
        if (isNotBlank(bannerForm.getTitle())) {
            banner.setTitle(bannerForm.getTitle());
        }
        banner.setDescription(nullToEmpty(bannerForm.getDescription()));
        if (isNotBlank(bannerForm.getUrl())) {
            banner.setUrl(bannerForm.getUrl());
        }
        if (!bannerForm.getFile().isEmpty()) {
            banner.setFile(bannerForm.getFile().getBytes());
        }
        if (bannerForm.getFlightId() != null) {
            banner.setFlight(flightService.findFlight(bannerForm.getFlightId()));
        }
    }

    public List<BannerForm> findBannerFormByFlight(String flightId) {
        return findBannersByFLightId(flightId).stream().map(this::bannerToBannerForm).collect(toList());
    }

    private List<Banner> findBannersByFLightId(String flightId) {
        return newArrayList(bannerRepository.findBannersByFlightId(Long.valueOf(flightId)));
    }

    private BannerForm bannerToBannerForm(Banner banner) {
        BannerForm result = new BannerForm();
        fillBannerForm(result, banner);
        return result;
    }

    private void fillBannerForm(BannerForm bannerForm, Banner banner) {
        bannerForm.setTitle(banner.getTitle());
        bannerForm.setUrl(banner.getUrl());
        bannerForm.setId(String.valueOf(banner.getId()));
        bannerForm.setDescription(nullToEmpty(banner.getDescription()));
        bannerForm.setFlightId(getStringFlightId(banner));
    }

    private String getStringFlightId(Banner banner) {
        return String.valueOf(banner.getFlight().getFlightId());
    }

    @Transactional
    public void delete(String bannerId) {
        bannerRepository.delete(Long.valueOf(bannerId));
    }

    public void editBanner(BannerForm bannerForm) throws IOException {
        checkArgument(checkUrlIfCorrect(bannerForm), "You don't have such site!!!");
        Banner banner = findBanner(bannerForm.getId());
        fillBanner(banner, bannerForm);
        save(banner);
    }

    private boolean checkUrlIfCorrect(BannerForm bannerForm) {
        return bannerForm.getUrl().startsWith(flightService.getFlightsURL(bannerForm.getFlightId()));
    }

    @Transactional
    private Banner findBanner(String id) {
        return bannerRepository.findOne(Long.valueOf(id));
    }

    public byte[] getBannerImage(String title) {
        return bannerRepository.findOneByTitle(title).getFile();
    }
}
