package lv.notes.ims.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class BadRequestDto extends ErrorResponseDto {
    private List<Field> fields = new ArrayList<>(5);

    @Getter
    @Setter
    @EqualsAndHashCode
    @Accessors(chain = true)
    public static class Field {
        private String name;
        private String value;
        private String details;
    }
}
