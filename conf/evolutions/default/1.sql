# campaigns,delivery_logs schema

# --- !Ups

CREATE TABLE campaigns (
    id integer(10) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    content varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE delivery_logs (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    campaign_id integer(10) NOT NULL,
    created_at datetime NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE campaigns;
DROP TABLE delivery_logs;

