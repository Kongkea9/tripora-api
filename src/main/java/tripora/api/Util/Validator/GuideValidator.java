package tripora.api.Util.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import tripora.api.Util.annotation.Phone;
import tripora.api.dto.GuideRequest;
import tripora.api.exception.BadRequestException;
import tripora.api.exception.ConflictException;
import tripora.api.repository.GuideRepository;

@Component
public class GuideValidator {

    public void validateCreate(GuideRequest req, GuideRepository repo) {

        if (req.name().trim().length() < 3) {
            throw new BadRequestException("Name too short");
        }

        if (req.phone() != null && repo.existsByPhone(req.phone())) {
            throw new ConflictException("Phone already exists");
        }

        if (req.bio() != null && req.bio().length() > 255) {
            throw new BadRequestException("Bio too long");
        }
    }

    public void validateUpdate(GuideRequest req) {

        if (req.name() != null && req.name().isBlank()) {
            throw new BadRequestException("Name cannot be blank");
        }
    }

    public class KhmerPhoneValidator implements ConstraintValidator<Phone, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            if (value == null || value.isBlank()) return true;


            String phone = value.replaceAll("[\\s-]", "");

            if (!(phone.startsWith("+855") || phone.startsWith("0"))) {
                return false;
            }

            if (phone.startsWith("+855")) {
                phone = phone.substring(4);
            } else if (phone.startsWith("0")) {
                phone = phone.substring(1);
            }


            if (!phone.matches("\\d+")) return false;

            return phone.length() >= 8 && phone.length() <= 9;
        }
    }
}