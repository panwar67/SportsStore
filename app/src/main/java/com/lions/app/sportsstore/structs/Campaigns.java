package com.lions.app.sportsstore.structs;

import java.io.Serializable;

/**
 * Created by Panwar on 28/01/18.
 */

public class Campaigns  implements Serializable
{
    String campaignName;
    String campaignUrl;
    String campaignId;
    String campaignType;

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getCampaignUrl() {
        return campaignUrl;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public void setCampaignUrl(String campaignUrl) {
        this.campaignUrl = campaignUrl;
    }

}
