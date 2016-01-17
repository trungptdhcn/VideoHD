package com.taa.videohd.sync.dailymotion;


import com.taa.videohd.sync.RequestDTO;

/**
 * Created by Trung on 11/25/2015.
 */
public class DailymotionRequestDTO extends RequestDTO
{
    private final String id;
    private final String keyWord;
    private final String fields;
    private final String playlistId;
    private final String flags;
    private final String sort;
    private final String type;
    private final String country;
    private final Integer longerThan;
    private final Integer shorterThan;
    private final Long createdAfter;
    private final Long createdBefore;
    private int page;
    private int limit;

    public DailymotionRequestDTO(DailymotionRequestBuilder builder)
    {
        this.id = builder.id;
        this.keyWord = builder.keyWord;
        this.fields = builder.fields;
        this.flags = builder.flags;
        this.sort = builder.sort;
        this.page = builder.page;
        this.limit = builder.limit;
        this.type = builder.type;
        this.country = builder.country;
        this.longerThan = builder.longerThan;
        this.shorterThan = builder.shorterThan;
        this.playlistId = builder.playlistId;
        this.createdAfter = builder.createdAfter;
        this.createdBefore = builder.createdBefore;
    }

    public String getKeyWord()
    {
        return keyWord;
    }

    public String getFields()
    {
        return fields;
    }

    public String getFlags()
    {
        return flags;
    }

    public String getSort()
    {
        return sort;
    }

    public int getPage()
    {
        return page;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public String getType()
    {
        return type;
    }

    public String getCountry()
    {
        return country;
    }

    public Integer getLongerThan()
    {
        return longerThan;
    }

    public Integer getShorterThan()
    {
        return shorterThan;
    }

    public String getPlaylistId()
    {
        return playlistId;
    }

    public String getId()
    {
        return id;
    }

    public Long getCreatedAfter()
    {
        return createdAfter;
    }

    public Long getCreatedBefore()
    {
        return createdBefore;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public static class DailymotionRequestBuilder
    {
        private final String keyWord;
        private String fields;
        private String flags;
        private String sort;
        private String type;
        private String country;
        private String playlistId;
        private Integer longerThan;
        private Integer shorterThan;
        private String id;
        private int page;
        private int limit;
        private Long createdAfter;
        private Long createdBefore;


        public DailymotionRequestBuilder(String keyWord)
        {
            this.keyWord = keyWord;
        }

        public DailymotionRequestBuilder fields(String fields)
        {
            this.fields = fields;
            return this;
        }

        public DailymotionRequestBuilder sort(String sort)
        {
            this.sort = sort;
            return this;
        }

        public DailymotionRequestBuilder flags(String flags)
        {
            this.flags = flags;
            return this;
        }

        public DailymotionRequestBuilder page(int page)
        {
            this.page = page;
            return this;
        }

        public DailymotionRequestBuilder limit(int limit)
        {
            this.limit = limit;
            return this;
        }

        public DailymotionRequestBuilder type(String type)
        {
            this.type = type;
            return this;
        }

        public DailymotionRequestBuilder country(String country)
        {
            this.country = country;
            return this;
        }

        public DailymotionRequestBuilder longerThan(Integer longerThan)
        {
            this.longerThan = longerThan;
            return this;
        }

        public DailymotionRequestBuilder shorterThan(Integer shorterThan)
        {
            this.shorterThan = shorterThan;
            return this;
        }

        public DailymotionRequestBuilder playlistId(String playlistId)
        {
            this.playlistId = playlistId;
            return this;
        }

        public DailymotionRequestBuilder id(String id)
        {
            this.id = id;
            return this;
        }
        public DailymotionRequestBuilder createdAfter(Long createdAfter)
        {
            this.createdAfter = createdAfter;
            return this;
        }
        public DailymotionRequestBuilder createdBefore(Long createdBefore)
        {
            this.createdBefore = createdBefore;
            return this;
        }

        public DailymotionRequestDTO build()
        {
            return new DailymotionRequestDTO(this);
        }
    }
}
