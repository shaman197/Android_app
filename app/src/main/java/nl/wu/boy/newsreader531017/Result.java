package nl.wu.boy.newsreader531017;

import java.util.List;

/**
 * Created by Boy on 9-10-2016.
 */
public class Result {
    public List<Article> Results;
    public int NextId;

    public Result(List<Article> Result, int NextId) {
        this.Results = Result;
        this.NextId = NextId;
    }
}