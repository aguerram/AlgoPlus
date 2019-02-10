import java.io.InputStream;

public class Loader {
	public static InputStream stream(String link)
	{
		return Loader.class.getResourceAsStream(link);
	}
}
