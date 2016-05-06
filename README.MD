## Just-Us Blog Backend

* A problem is that we would have different related model with duplicated data, it's not dry,
eg. UserSubmitPost, BlogPost, UserSubmitPostUpdate

* Caveat learned, do not use connection pool in test, because test framework might decide to instantiate connection pool
for each test case, resulting lot's of connection created and hit db limits

* Need to setup CI build with docker

* HTTP related config like CORS, Allow-access-header-expose, should be made configurable without code change

* Do not use Slick for non trivial db query, SQL is probably more readable and easier to debug
