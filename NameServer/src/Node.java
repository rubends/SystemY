public class Node {


    public Node(String filename, String ipAddress) {
        this.filename = filename;
        this.ipAddress = ipAddress;
        this.hash = Math.abs(filename.hashCode() % 32768);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    String filename;
    int hash;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    String ipAddress;

}
