package nl.wu.boy.newsreader531017;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Boy on 9-10-2016.
 */
public class Article implements Parcelable {
    public int Id;
    public int Feed;
    public String Title;
    public String Summary;
    public String Image;
    public String Url;
    public List<String> Related;
    public List<Category> categories;
    public boolean IsLiked;

    public Article(int Id, int Feed, String Title, String Summary, String Image, String Url, List<String> Related, List<Category> categories, boolean IsLiked) {
        this.Id = Id;
        this.Feed = Feed;
        this.Title = Title;
        this.Summary = Summary;
        this.Image = Image;
        this.Url = Url;
        this.Related = Related;
        this.categories  = categories;
        this.IsLiked = IsLiked;
    }

    protected Article(Parcel in) {
        Id = in.readInt();
        Feed = in.readInt();
        Title = in.readString();
        Summary = in.readString();
        Image = in.readString();
        Url = in.readString();
        Related = in.createStringArrayList();
        IsLiked = in.readByte() != 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(Feed);
        dest.writeString(Title);
        dest.writeString(Summary);
        dest.writeString(Image);
        dest.writeString(Url);
        dest.writeStringList(Related);
        dest.writeByte((byte) (IsLiked ? 1 : 0));
    }
}