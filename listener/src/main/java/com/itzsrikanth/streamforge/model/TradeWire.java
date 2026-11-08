package com.itzsrikanth.streamforge.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeWire {
    @JsonProperty("e")
    public String eventType;

    @JsonProperty("E")
    public long eventTimestamp;

    @JsonProperty("s")
    public String symbol;

    @JsonProperty("t")
    public long tradeId;

    @JsonProperty("p")
    public String price;

    @JsonProperty("q")
    public String quantity;

    @JsonProperty("T")
    public long tradeTime;

    @JsonProperty("m")
    public boolean isMarketMaker;

    @JsonProperty("M")
    public boolean M;
}
