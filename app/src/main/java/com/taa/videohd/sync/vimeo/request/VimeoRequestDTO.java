package com.taa.videohd.sync.vimeo.request;



import com.taa.videohd.sync.RequestDTO;

import java.io.Serializable;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoRequestDTO extends RequestDTO implements Serializable
{
    private final String keyWord;
    private final String id;
    private final String category;
    private final String sort;
    private final String direction;
    private final String filter;
    private final String type;
    private String pageToken;

    public VimeoRequestDTO(VimeoRequestBuilder builder)
    {
        this.keyWord = builder.keyWord;
        this.id = builder.id;
        this.category = builder.category;
        this.sort = builder.sort;
        this.direction = builder.direction;
        this.filter = builder.filter;
        this.pageToken = builder.pageToken;
        this.type = builder.type;
    }

    public String getKeyWord()
    {
        return keyWord;
    }

    public String getSort()
    {
        return sort;
    }

    public String getPageToken()
    {
        return pageToken;
    }

    public void setPageToken(String pageToken)
    {
        this.pageToken = pageToken;
    }

    public String getDirection()
    {
        return direction;
    }

    public String getFilter()
    {
        return filter;
    }

    public String getCategory()
    {
        return category;
    }

    public String getType()
    {
        return type;
    }

    public String getId()
    {
        return id;
    }

    public static class VimeoRequestBuilder
    {
        private final String keyWord;
        private String id;
        private String category;
        private String filter;
        private String direction;
        private String sort;
        private String type;
        private String pageToken;

        public VimeoRequestBuilder(String keyWord)
        {
            this.keyWord = keyWord;
        }

        public VimeoRequestBuilder sort(String sort)
        {
            this.sort = sort;
            return this;
        }

        public VimeoRequestBuilder type(String type)
        {
            this.type = type;
            return this;
        }

        public VimeoRequestBuilder filter(String filter)
        {
            this.filter = filter;
            return this;
        }

        public VimeoRequestBuilder direction(String direction)
        {
            this.direction = direction;
            return this;
        }

        public VimeoRequestBuilder category(String category)
        {
            this.category = category;
            return this;
        }

        public VimeoRequestBuilder id(String id)
        {
            this.id = id;
            return this;
        }

        public VimeoRequestDTO build()
        {
            return new VimeoRequestDTO(this);
        }

    }

}
