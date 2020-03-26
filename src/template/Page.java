package template;

import java.util.ArrayList;
import java.util.List;

public class Page {
    List<Album> albumList;

    public Page(List<Album> albums){
        albumList = albums;
    }

    public List<Album> getAlbumList(){
        return albumList;
    }

    public void setAlbumList(List<Album> a){
        albumList = a;
    }
}
