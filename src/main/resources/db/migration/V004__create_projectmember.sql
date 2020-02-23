CREATE TABLE projectmembers(
memberid INTEGER NOT NULL,
projectid INTEGER NOT NULL,
PRIMARY KEY (memberid, projectid),
FOREIGN KEY (memberid) references members (id),
FOREIGN KEY (projectid) references projects (id)
);