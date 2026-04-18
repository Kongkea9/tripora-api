package tripora.api.dto;

public record GuideResponse(
        Integer id,
        String name,
        String bio,
        String photoUrl,
        String phone,
        Boolean isAvailable
) {}