package bus;

import java.util.Map;

import lombok.Data;

@Data
public class ApiResponse {

	public long status;

	public String msg;

	public long time;

	public Map<String, Object>[] data;
}
