package purihuaman.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryDTO {
  private UUID categoryId;
  private String categoryName;
}
