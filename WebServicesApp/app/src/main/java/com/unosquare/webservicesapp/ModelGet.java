package com.unosquare.webservicesapp;

/**
 * Created by admin on 18/10/2014.
 */
public class ModelGet {
    private Query query;

    public Query getQuery() {
        return query;
    }

    public class Query {
        private Results results;

        public Results getResults() {
            return results;
        }

        public class Results{
            private Channel channel;

            public Channel getChannel() {
                return channel;
            }

            public class Channel{
                private Item item;

                public Item getItem() {
                    return item;
                }

                public class Item{
                    private Forecast forecast[];

                    public Forecast[] getForecast() {
                        return forecast;
                    }

                    public class Forecast{
                        String code;
                        String date;
                        String day;
                        String high;
                        String low;
                        String text;

                        public String getCode() {
                            return code;
                        }

                        public String getDate() {
                            return date;
                        }

                        public String getDay() {
                            return day;
                        }

                        public String getHigh() {
                            return high;
                        }

                        public String getLow() {
                            return low;
                        }

                        public String getText() {
                            return text;
                        }
                    }

                }

            }
        }
    }

}
