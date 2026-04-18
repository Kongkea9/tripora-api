package tripora.api.service.guide;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripora.api.Util.Validator.GuideValidator;
import tripora.api.domain.Guide;
import tripora.api.dto.GuideRequest;
import tripora.api.dto.GuideResponse;
import tripora.api.exception.ConflictException;
import tripora.api.exception.ResourceNotFoundException;
import tripora.api.mapper.GuideMapper;
import tripora.api.repository.GuideRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GuideServiceImpl implements GuideService {

    private final GuideRepository guideRepository;
    private final GuideValidator guideValidator;
    private final GuideMapper guideMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GuideResponse> getAll() {
        return guideRepository.findAll()
                .stream()
                .map(guideMapper::mapToResponse)
                .toList();
    }

    @Override
    public GuideResponse create(GuideRequest req) {

        guideValidator.validateCreate(req, guideRepository);

        String normalizedPhone = normalizePhone(req.phone());

        if (guideRepository.existsByPhone(normalizedPhone)) {
            throw new ConflictException("Phone already exists");
        }

        Guide guide = new Guide();
        guide.setName(req.name().trim());
        guide.setBio(req.bio());
        guide.setPhotoUrl(req.photoUrl());
        guide.setPhone(normalizedPhone);
        guide.setIsAvailable(req.isAvailable());

        return guideMapper.mapToResponse(guideRepository.save(guide));
    }

    @Override
    public GuideResponse update(Integer id, GuideRequest req) {

        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));

        guideValidator.validateUpdate(req);

        if (req.name() != null) guide.setName(req.name().trim());
        if (req.bio() != null) guide.setBio(req.bio());
        if (req.photoUrl() != null) guide.setPhotoUrl(req.photoUrl());

        if (req.phone() != null) {
            String normalizedPhone = normalizePhone(req.phone());

            if (guideRepository.existsByPhone(normalizedPhone)) {
                throw new ConflictException("Phone already exists");
            }

            guide.setPhone(normalizedPhone);
        }

        if (req.isAvailable() != null) guide.setIsAvailable(req.isAvailable());

        return guideMapper.mapToResponse(guideRepository.save(guide));
    }
    @Override
    public void delete(Integer id) {

        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));

        guideRepository.delete(guide);
    }


    private String normalizePhone(String value) {
        if (value == null) return null;

        String phone = value.replaceAll("[\\s-]", "");

        if (phone.startsWith("+855")) {
            phone = phone.substring(4);
        } else if (phone.startsWith("0")) {
            phone = phone.substring(1);
        }

        return "+855" + phone;
    }
}
