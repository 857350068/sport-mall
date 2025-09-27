import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class UpdateCDNLinks {
    public static void main(String[] args) {
        // Define new CDN links
        String newCssLink = "<link href=\"https://cdn.jsdelivr.net.cn/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">";
        
        String newJsBundleLink = "<script src=\"https://cdn.jsdelivr.net.cn/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>";
        
        String newPopperLink = "<script src=\"https://cdn.jsdelivr.net.cn/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js\" integrity=\"sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r\" crossorigin=\"anonymous\"></script>";
        
        String newJsLink = "<script src=\"https://cdn.jsdelivr.net.cn/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js\" integrity=\"sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy\" crossorigin=\"anonymous\"></script>";
        
        // Directory to process
        String templatesDir = "src/main/resources/templates";
        
        try {
            updateDirectory(new File(templatesDir), newCssLink, newJsBundleLink, newPopperLink, newJsLink);
            System.out.println("All HTML files CDN links have been updated successfully!");
        } catch (IOException e) {
            System.err.println("Error during update process: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void updateDirectory(File directory, String newCssLink, String newJsBundleLink, String newPopperLink, String newJsLink) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    updateDirectory(file, newCssLink, newJsBundleLink, newPopperLink, newJsLink);
                } else if (file.getName().endsWith(".html")) {
                    updateHtmlFile(file, newCssLink, newJsBundleLink, newPopperLink, newJsLink);
                }
            }
        }
    }
    
    private static void updateHtmlFile(File file, String newCssLink, String newJsBundleLink, String newPopperLink, String newJsLink) throws IOException {
        // Read file content
        String content = new String(Files.readAllBytes(file.toPath()));
        
        // Update CSS link
        content = replaceCssLink(content, newCssLink);
        
        // Update JS links
        content = replaceJsLinks(content, newJsBundleLink, newPopperLink, newJsLink);
        
        // Write updated content
        Files.write(file.toPath(), content.getBytes());
        System.out.println("Updated: " + file.getPath());
    }
    
    private static String replaceCssLink(String content, String newCssLink) {
        // Match existing Bootstrap CSS links (possibly different CDNs)
        String cssPattern = "<link\\s+href\\s*=\\s*\"[^\"]*bootstrap[^\"]*\\.css[^\"]*\"[^>]*>";
        return content.replaceAll(cssPattern, Matcher.quoteReplacement(newCssLink));
    }
    
    private static String replaceJsLinks(String content, String newJsBundleLink, String newPopperLink, String newJsLink) {
        // Remove existing Bootstrap JS links (possibly different CDNs)
        String jsPattern = "<script\\s+src\\s*=\\s*\"[^\"]*bootstrap[^\"]*\\.js[^\"]*\"[^>]*></script>";
        content = content.replaceAll(jsPattern, "");
        
        // Remove existing Popper.js links (possibly different CDNs)
        String popperPattern = "<script\\s+src\\s*=\\s*\"[^\"]*@popperjs[^\"]*\\.js[^\"]*\"[^>]*></script>";
        content = content.replaceAll(popperPattern, "");
        
        // Insert new JS links before </body> tag
        String bodyEndPattern = "</body>";
        String newJsLinks = newJsBundleLink + "\n    " + newPopperLink + "\n    " + newJsLink;
        return content.replaceFirst(bodyEndPattern, Matcher.quoteReplacement(newJsLinks + "\n</body>"));
    }
}