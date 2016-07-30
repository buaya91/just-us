--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: posts; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE posts (
    pid integer NOT NULL,
    author text,
    title text NOT NULL,
    content text NOT NULL,
    post_at date DEFAULT ('now'::text)::date NOT NULL,
    tags text[] DEFAULT '{}'::text[] NOT NULL
);


--
-- Name: posts_pid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE posts_pid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: posts_pid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE posts_pid_seq OWNED BY posts.pid;


--
-- Name: users; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE users (
    email text NOT NULL,
    password text NOT NULL,
    name text NOT NULL
);


--
-- Name: pid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY posts ALTER COLUMN pid SET DEFAULT nextval('posts_pid_seq'::regclass);


--
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: -
--

COPY posts (pid, author, title, content, post_at, tags) FROM stdin;
1	l.q.wei91@gmail.com	Why I am starting a blog?	This is my 1st blogpost on this blog, this blog is shared between me and my girlfriend.\n\nI built it because\n\n* I love building stuffs\n* I'd like to have some projects/hobby that my girlfriend could be involve, and having a blog shared by us sounds like a good idea, she could come up with design idea, and I'll implement them, we get to discuss, and this will be a great experience\n* I hope this blog could become a portal of all my projects and visitor can run those project directly through this portal, thus it's better for me to have full control over the system.\n\n\nThe source of this blog is available [here](https://github.com/buaya91/just-us-blog)	2016-05-29	{self}
3	l.q.wei91@gmail.com	Redux best practices	I've been using ReactJS professionally for quite sometime, so I intend to record some of my findings and hope someone finds it useful\n\n\n## 1. setState for local UI state\nCompared to storing everything in redux's `store`, letting Component to handle their own has the benefit of \n* more direct, `setState -> render -> shouldComponentUpdate` instead of `action -> store -> render -> shouldComponentUpdate` \n* more readable as you can know what happen to the state just by looking at the Component's code, without digging into the action and reducer.\n\nan example of local UI state would be whether a `checkbox` is checked or not, `{checked: true}`\n\n## 2. Reducer should be pure\nThis is being repeated in quite a lot of places, one reason being impure reducer will break the time travel functionality of redux-dev-tools, as you cannot revert side effect.\n[More details](https://github.com/rackt/redux/blob/master/docs/basics/Reducers.md)\n\n## 3. Use Redux-Saga for side-effects\nActions should be the only way Component interact with outside world (store, ajax or websocket call). This will simplify the component, and provide a simpler, less buggy architecture. \n\nOne way perform side effects via actions is to use *Action Creator* with [redux-thunk](https://github.com/gaearon/redux-thunk), but soon you will find thunk being unwieldy as actions are now very complex, and difficult to test, and complex stuff should be tested.\n\nA better alternative would be [redux-saga](https://github.com/yelouafi/redux-saga), it is subjectively cleaner and objectively more testable.\n\n## 4. Smart Component vs Dumb Component\nSeparate components into two categories, \n* Smart - Components that have direct access to store and actions\n* Dumb - Components that have no direct access to store and actions\nThis allow Dumb components to be highly composable and seldom need to be change.\n\nOne rule of thumb is \n> Smart component for concrete implementation, eg. `Header of sites`\n\n> Dumb component for more generic usage, eg. `container type component`\n\nThere are a few more rules to be consider, kindly check link below.\n[More details](https://medium.com/@dan_abramov/smart-and-dumb-components-7ca2f9a7c7d0#.20fvey2us)\n\n## 5. Data reshape\nIn many cases, the external data model will not match your app's internal model requirement, \nexample external api return an array `['Female', '178cm', '55kg']` , but you want `{gender: 'F', height: '178', weight: '55'}`\n\nso where should we perform this restructure?\n\nWe have 4 options:\n* In selectors\n* In component\n* In reducer\n* In actionCreators or Saga\n\nGenerally we should reshape data either in `reducer` or `saga / actionCreator`, because we want to have easy to use data model as soon as possible. And since the data source (reducer) are being shared, operations on the data will likely be reusable for different consumer.\n\n## 6. Selectors should follow SRP\nSelectors should only have 1 reason to be triggered, if selectors have more than 1 reason to be triggered, some of the computation would be meaningless, it will not affect program correctness if things are pure, but it would means unnecesary computations.\n	2016-06-02	{programming,reactjs}
2	suhui1128@gmail.com	The longer relationship lasts, the lesser common memories, isn't it?	As the saying goes, "A real woman can do it all by herself, but a real Man won't let her." \n\nHonestly, I am really a demanding girlfriend to my love. However, if being a girlfriend cannot even request, why do we still need a man. Please take note I am asking for a MAN, not just a BOY to be my boyfriend.\n\n![pic](https://scontent-ams3-1.cdninstagram.com/t51.2885-15/s640x640/sh0.08/e35/12501625_536492306554191_559254713_n.jpg?ig_cache_key=MTIxNTI4NTE5ODI3ODAyNjM0OQ%3D%3D.2.l)\n \nThank you to my love for making this blog comes true. It's our turn to shine the world. \n\nStay tune ;)	2016-05-06	{sicklyxpillpopper}
4	l.q.wei91@gmail.com	What I learn by building a blog	This post will record some technical details of this blog and what I've learned in the process.\n\n<br/>\n\n## Technical Overview\nThe whole app is divided into front and backend and they communicate through HTTP API.\n\n| Backend Stack |\n| -------------- | ---- |\n| Programming language | [Scala](http://www.scala-lang.org/) |\n| Rest api library | [Akka-http](http://doc.akka.io/docs/akka/2.4.4/scala/http/) |\n| Database | [Postgresql](http://www.postgresql.org/) |\n| ORM/FRM |  [Slick](http://slick.lightbend.com/) |\n  \n</br>\n\n| Frontend Stack |\n| -------------- | ---- |\n| Programming language | Javascript (`As if I have other choices[1]`) |\n| UI library | [ReactJS](https://facebook.github.io/react/) |\n| State library | Redux |\n| Web framework | Express |\n\n![](http://buaya91.github.io/just-us-blog/blog-overview.jpg)\n\n\n## Things I've learned\n| Web |\n| ----- |\n| CORS (Cross Origin Resource Sharing) | Browser disallow cross-origin request by default to prevent a variety of xss vulnerabilities, to allow, `Access-Control-Allow-Origin: xxx` is needed |\n| Access-control-expose-headers | When using CORS, to read header of cors-enabled response, `access-control-expose-headers` is required to specify which header to expose to headers |\n| JWT | A new mechanism of session handling, where some data are signed by a secret key known by server, and then be encoded, the result is the token. The token can then serve as a session. |\n| [Material UI](http://www.material-ui.com/#/) | A convenient library to provide many commonly used UI widget in a composable way, this complement React pretty well, as React provide easy way to compose UI, but building them from scratch can still be a pain |\n| Color scheme | As a backend guy, I actively avoid making UI decision from time to time, but not this time, since I fully own the project, I learn a little bit about complementary colors, analogous color and how to pick 3 colors that play well from the color wheel |\n\n<br/>\n\n| ES6/7 (Javascript) |\n| ------ |\n| Generators | This is not a new concept, but I never had a close look on it until very recent, generator is useful to model side effect, it is the fundamental construct that power redux-saga. |\n\n<br/>\n\n| Scala |\n| ----- |\n| Scalatest | Should disable db connection pooling when running test, as test framework does not release connection until test finish |\n	2016-05-25	{programming}
10	suhui1128@gmail.com	What to do when your GirlFriend is angry? 	Dear gentleman, you might wonder what to do when Your Majesty pissed off. \n\n<center><img src="https://scontent-sin1-1.xx.fbcdn.net/v/t1.0-9/13432270_10209580848138914_7770141926128912423_n.jpg?oh=54dc60cd161c7d386cb017f347c2325e&oe=57FE754C" width="600" height="400" /></center>\n\nJust do this, bro! It is going to work because I'm a lady too! \n\n<i class="icon-pencil"></i>*** Do Not ***\n- Ask why - it will make her angrier as the lady is expecting you to know her heart although we don't \n- Laugh or Tease or even a Simple Smile - it will make her slaps on you because she feels you aren't serious \n- Rebut no matter how strong or valid your reasons are because to her, these are all excuses\n- Run away - it proves you are a coward! :P\n\n<i class="icon-pencil"></i>*** Do ***\n- Hug her no matter how hard she pushes you away - it is a sign of acceptances\n- Buy her food - maybe she is just hungry\n- Listen to her - just keep silences, let her words flow through. She doesn't mean anything harsh, take it lightly.\n\nAfter all, it can be just girls' problem. However, if you aren't smart enough, it will soon become your love problem. 	2016-07-28	{BuildLove}
6	suhui1128@gmail.com	影评 － Me Before You #liveboldly	我问：“整部戏你最感动的部分是？” \n他说：“这是部感人的电影但让我更难过的是Will Treynor 的绝望与彻底放弃..." （还真够跳tone）\n我想了下，最感动me的部分除了整段结尾所表达的#liveboldly， 更让我最佩服的是Louisa愿意陪着自已心爱的人走到最后的勇气。\n\n"I realized I was afraid of living without him. How is it you have the right to destroy my life, I wanted to demand of him but I'm not a say in yours? But I had promised. - Louisa Clark“\n\n爱情是什么？\nWill Treynor 说是 Louisa Clark 带来的欢乐 但 自己却给不了她值得的欢乐。 \nLouisa Clark 说是 Will Treynor 让我重生 但 我却给不了他继续活下去的理由。\n\n致所有给这部戏的结论为“有钱就是任性” 的酸民们，\n如有心仪对象，切勿约电影，尤其是这一类有机会让你递纸巾、温柔抱抱的爱情文艺片。这样的taste 与品论还真是大煞风景，呸！\n－ 还抱有点少女心态的影迷上\n\n![pic](https://scontent-sin1-1.xx.fbcdn.net/v/t1.0-9/13332722_10209513623618343_3956044575574167691_n.jpg?oh=6e0f2ae6d37401e61efa82b964f42a96&oe=580E3054)\n\n	2016-06-11	{MovieReviews}
5	suhui1128@gmail.com	如果婚姻定了有效期限，你同意吗？	我相信每个女孩儿都曾经幻想过自己披着白纱与王子手牵着手，在幸福的祝福声中打开所谓爱情的殿堂大门，步入婚姻的那一刻。但对于经历过父母离异影响了家庭生活，忍受过冷暴力的我来说，对婚姻始终恐惧。越接近适婚年龄，即便是有了个准备好的王子，那些年女孩儿做过的梦，始终泡影。\n\n看了“奇葩说”讨论这个题目，听了那么多“七年之痒”的故事，我的第一反应是绝对的正方，支持让婚姻的契约关系定在一段期限。合得来，约满续约, 浪漫的情侣会有七年一次的婚礼; 不合的话则等时间生效，自然而然的分道扬镳也无须忍受旁人的冷嘲热讽, 不用等到大难领头也能各自飞吧! 没压力, 没辜负, 没谁对不起谁, 有啥不好? \n\n但反方辩手-最佳好闺蜜- 花希的立论让我动摇了！<重庆森林>王家卫导演说：“不知道什么时候，人类把越来越多事情都加了期限”。我买东西时也不怎么在乎标签，所以也希望人生中里有些事可以不在乎时间，而我更希望那是爱情。因为两个没血缘，对彼此没义务的人，因为爱，有了勇气，给了承诺。\n\n![](https://scontent-kul1-1.xx.fbcdn.net/v/t1.0-9/13321728_10209446217813240_6724088497377150973_n.jpg?oh=2fca33a08b4b37bec3412603d2812e1c&oe=57E37F9F)\n\n	2016-07-28	{marriage}
7	l.q.wei91@gmail.com	Tutorial on akka http websocket push 	Recently I need to achieve websocket push in akka http and found out it was not very well documented, there are some pieces scattered in the internet here and there. \n\nSo I decided to write this short tutorial to show how to achieve that.\n\nBefore start reading, here's a quick demo.\n<iframe src="/ws-push" allow-forms height="350" width="500"></iframe>\n\nClick <span style="color: orange">connect</span> will establish a websocket connection, which server will push some random number to client.\n\n|  Dependency   |  Version    |\n| ------------- | ----- | \n| Akka | 2.4.7 |\n| Scala | 2.11.8 |\n\nThis tutorial assume you have basic knowledge on [akka-http](http://doc.akka.io/docs/akka/current/scala/http/routing-dsl/index.html)\n\nWe want to host an url that accepts websocket connection, and once connection established, it will constantly push some data to client, client request from websocket connection will be ignore for the sake of this tutorial.\n\n### Websocket route\nLet's start by defining a route using akka-http DSL\n> ```\n>   val route = path("ws-push") {\n>     handleWebSocketMessages(serverPushHandler)\n>   }\n> ```\n\nNotice the signature of `handleWebSocketMessages`\n> ```\n> def handleWebSocketMessages(handler: Flow[Message, Message, Any]): Route\n> ```\n\nIt accepts a Flow[Message, Message, Any], the good old request response mechanism that we are familiar with, but it's not very obvious if we could use it to implement a websocket endpoint that do server push.\n\nIt turns out we could, and it pretty easy, so our task is to define a `Flow[Message, Message, Any]` that send out data independent to client request, and ignore all client request.\n\n### Flow implementation\n\n```\ndef serverPushHandler: Flow[Message, Message, NotUsed] = \n  Flow.fromGraph(GraphDSL.create() { \n    implicit b =>\n      import GraphDSL.Implicits._\n\n      val ignoreInput = b.add(Sink.ignore)\n      val actor = Source.actorPublisher[String](Props[WSPushActor])\n\n      val randomOutputSource = b.add(actor)\n      val stringToMsg = b.add(Flow[String].map(s => TextMessage(s)))\n\n      randomOutputSource ~> stringToMsg\n\n      FlowShape(ignoreInput.in, stringToMsg.out)\n  })\n```\n\nLet's go through them step by step\n\n#### ```Flow.fromGraph(GraphDSL.create() ... ```\nGraph is an important concept in akka-stream, you can think a Graph as a collection of interconnected processing stages, it defines the topology of processing stages. `Flow.fromGraph` is a helper method that create Flow from Graph.\n\n```GraphDSL.create``` is a curried function that allow us to define custom processing stage and connect them the way we want\n\nHere's a simplified signature\n\n>> ```\n>>  def create()(buildBlock: (Builder[NotUsed]) ⇒ S): Graph[S, NotUsed]\n>> ```\n\nWe need to define correct implementation of `buildBLock`, which has a signature of \n```Builder[NotUsed] ⇒ S```\n\n\n1. ```\n implicit builder =>\n import GraphDSL.Implicits._ \n```\nBoilerplate\n\n2.  ```\n val ignoreInput = builder.add(Sink.ignore)\n```\nDefine a processing stage that ignore incoming data, Sink is a processing stage that have 1 input port\n\n3.  ```\n val actor = Source.actorPublisher[String](Props[WSPushActor])\n```\nHere, we create a Source from an actorPublisher, so when we materialize/run this Source, it will spawn an actor, which that actor can emit data into the Source, note that from this statement we can tell that the Source will emit String.\nWe will go into details of the actor later\n\n4.  ```\n val randomOutputSource = builder.add(actor)\n```\n`builder.add` will create an immutable processing stage to be used in this Graph\n\n5.  ```\n val stringToMsg = b.add(Flow[String].map(s => TextMessage(s)))\n```\nThis is a processing stage that takes input and convert it into TextMessage\n\n6. ```\nrandomOutputSource ~> stringToMsg\n```\nHere we make use of the pretty `~>` which means connect this 2 processing stage, so data emitted by Source will be pump into `stringToMsg` pipe\n\n7. ```\nFlowShape(ignoreInput.in, stringToMsg.out)\n```\nFinally, we return a shape that looks like Flow, so that it can later be converted into Flow, a Flow has one input port and one output port, which is obtained by `ignoreInput.in` and `stringToMsg.out` respectively.\n\nSo finally we would have a Flow[Message, Message, _] that will ignore incoming Message, and have an actor that control pushing data down to websocket client.\n\nThe last piece of the puzzle is the Actor, which we created with `Source.actorPublisher`, below is the implementation.\n\n\n### Actor publisher\n> ```\n>  // ActorPublisher[String] that publish string\n> class WSPushActor extends ActorPublisher[String] {\n      \n>   // 1. a scheduler that send "Lucky number"\n>   //     to this actor every 1 second\n>   val tick = context.system\n>      .scheduler.schedule(1 second, 1 second, self, "Lucky number")\n\n>   // 2. a scheduler that send\n>   //  "Cancel" to this actor after 1 minute\n>   val cancelAfter1Min = context.system\n>       .scheduler.scheduleOnce(1 minutes, self, "Cancel")\n>   override def receive: Receive = {\n>     case "Cancel" => {   // 3. if receive "Cancel"\n>       onNext("Ended")    // cancel the tick scheduler\n>       tick.cancel()\n>     }\n>     case msg: String => {\n>       if (isActive && totalDemand > 0) {\n>         val random = Random.nextInt()\n>         val s = s"${msg} $random"\n>         onNext(s)               // publish a random Int to Source\n>       }\n>     }\n>   }\n> }\n> ```\n\nTwo points\n* calling onNext will publish data to subscriber, in our case the Source\n* calling onNext might result in runtime exception if actor is not active or downstream have no demand, which can be check by `isActive` and `totalDemand`, this is to maintain backpressure\n\nSo, by using an actor publisher, you can hook the websocket stream to any of your existing actor system.\n\n### Conclusion\nAkka-stream is an awesome library, but it certainly is confusing to beginner like me, I think we need more accessible documentation to describe relationship between each commonly used abstraction. I am starting to understand some of it, and it starts to shine due to composability and well integration with actor system.\n\n### References:\n* http://www.smartjava.org/content/create-reactive-websocket-server-akka-streams\n* http://doc.akka.io/docs/akka/2.4.7/scala/stream/stream-composition.html\n* http://doc.akka.io/api/akka/current/#akka.stream.actor.ActorPublisher	2016-06-12	{programming}
9	suhui1128@gmail.com	影评 － Love Rosie #dontfightthefeeling	<div style="text-align: center;"> \n\n<iframe width="560" height="315" src="https://www.youtube.com/embed/W8E1s-XN90c" frameborder="0" allowfullscreen></iframe>\n\n      <br />\n      <br />\n    </div>\n\n这是一个一直错过的故事。男女主角爆表的颜值绝对会让你忘了剧情是多么的梦幻，多么的不可思议 （窃笑：P）\n\n男女主角是青梅足马，随着成长，一起步入叛逆期与青春期。他俩的感情甚至好到要相互比较谁的处男／女之身更快一步被夺走。正值青春期的男生角果然聪明绝顶，能约到女神级女主角一起私奔美国求学，却也大方到让另一个白痴用9秒抹煞了她的初夜。更夸张的是还能怀上可爱宝宝让女神深陷未婚身子－年轻辣妈－放弃学习－委屈就业－还被无论长相、身材、事业、男人都赢过自己的昔日情敌constantly remind you how f*ck is your life 的恶性循环中。\n\n问世间男人能否与红颜知己只是相知相惜？\n问世间女人能否不留恋闺蜜麻吉的呵护守候？\n\n姐妹们，你确定男人不是因为一直爱着红颜，所以硬要说她是他best friend，让她的友谊与你的爱情并列？\n兄弟们，你确定女人不是因为缺乏安全感，所以誓死维护备胎麻吉的第一顺位，等哪一天要是没了你，确保他能马上替补换位？\n\n我不fancy罗曼蒂克，so没为这部电影的一直错过而感动。\n若你要欣赏这部电影，记得怀抱着100%的少女偶像剧情怀哦！\n\n所以，这个故事再一次警告情侣们，男人和女人之间做不到盖棉被、纯聊天的纯友谊呐！（无奈～）\n\n	2016-06-11	{MovieReviews}
8	l.q.wei91@gmail.com	Why learning math is hard?	Recently, I am trying to pick up some basic math, which is needed in my side project.\n\nI was pretty good in mathematics in high school, so I was expecting I am able to pick things up relatively quick and then jump back to my coding project to proceed.\n\nAnd things turn out quite the opposite (Like always), in the learning process, I formed an opinion that Math is difficult because the way we learn math is the opposite of the way we learn other subjects. \n\nFor example when learning biology, we first started from the environment.\n\n### This is a tiger\n![tiger](http://assets.worldwildlife.org/photos/1620/images/carousel_small/bengal-tiger-why-matter_7341043.jpg?1345548942)\n\n### This is also a tiger\n![tiger](http://cdn.ngkids.co.uk/dynamic/features_legacy/content/gallery/20140117081955719639878.jpg)\n\n### This is a ... \n![](http://img.chan4chan.com/img/2012-02-28/how-many.jpg)\n\nWe learn by looking at different objects that represent the same concept repeatedly, and then our brain will attempt to generalize over them to form knowledge over a specific concept. After all, a big part of our brain is a pattern matching engine.\n\nBut when it comes to Mathematics, things are pretty different, here I am referring to College Level Math.\n\nThe syllabus generally starts with definition of certain concepts, for example `What is Vector`, and build a reasoning system from there. After you have digested those material, you will start to spot Math in many places in real world [Math is everywhere](https://www.youtube.com/watch?v=lGQ8MPMYZ2I)\n\nThe problem is many if not most people never actually digest those highly abstract material, because the learning process is not what we've used to. \n\n> Most of the time, we learn from concrete examples, and form abstract concepts, so that we could apply them in real world scenario\n\n> In learning Math, we first learn some abstract concepts, then only we try to map them back to the real world.\n\nWhich I believe is one of the factor Math got it's reputation, I propose we could improve it, instead of teaching from basic concepts, we should \n\n1. show students a real world problem\n2. reduce it into a mathematical problem\n3. propose a new math concept that solve the problem.\n\nI speculate such method would provide 2 merits.\n\n1. boost student motivation of learning, as the relationship between the material and the real world is clear.\n2. it provides a real world reference for mathematical concepts, which means students can always refer to real world concrete examples when thinking of abstract concepts, less chance to lost in the abstract world.\n\n\n## Thanks for reading\n## Happy Learning ~!!\n\nPS: I am not an educator nor a Math expert, if I made any mistake in this article, please correct me =)	2016-07-07	{learning}
11	suhui1128@gmail.com	1/4 Life Crisis - Career	This is the hottest topic that I always wish to bring up during the gatherings with those long-time-no-see-mates. I am in my 25th in this year and it's already July now! OMG! 2016 has been coming to the second half, where am I? \n\nBelow are the wonders.\n<center><img src="https://scontent-sit4-1.xx.fbcdn.net/v/t1.0-9/13782263_10209962165191602_7655028526779565048_n.jpg?oh=bb3b012fa8bef0f9918ca0f1d535d91e&oe=58349A59" width="600" height="400" /></center>\n\n### Work or Passion \n \n"I really want to quit my job!" Ask yourself, really? Let's shift the focus to the factors that make you think in such way. By having 6 jobs in 4 years will not get you closer to any career which closes to passion. \n\n### Feel VS Thought \n\n"I feel I did not improve from my current job but I don't think I can get similar salary elsewhere!" Ask yourself again, which matters the most, learning & growth or money?\n\n### Suppose =/ Future \n\n"Am I suppose to buy a house now to at least secure my future?" Well, I assume you are earning not bad now to have such thought during 20s, congratulations! (at least you have money =P ) Before you make such decision, think about all those commitments aka burdens - monthly instalment and the interests VS freedom - free to make decisions. \n\n### Expenses > Income \n\nIt does not happen in only 20s. As you live longer, the more bills you have to pay so start to learn financial planning now. \n\n### Think > Do \n\nThis is the part you will find out where you are. \nAn action oriented person who takes future in your hands or the Sun Tzu strategist who knows the art of war but did not feel the challenging excitement. \n\n### Although I keep my positivity on all those mentioned, I miss the age when I thought I would have all my shit together by the time I was the age I am now.	2016-07-28	{self}
\.


--
-- Name: posts_pid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('posts_pid_seq', 11, true);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

COPY users (email, password, name) FROM stdin;
l.q.wei91@gmail.com	$2a$10$JhbPAWpRzUZekd/xxigCruaWnnyZeV2roLqenCfNpo5IBjgfZMcVC	Qingwei
suhui1128@gmail.com	$2a$10$JhbPAWpRzUZekd/xxigCruaWnnyZeV2roLqenCfNpo5IBjgfZMcVC	Suhui
\.


--
-- Name: posts_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (pid);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (email);


--
-- Name: posts_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT posts_author_fkey FOREIGN KEY (author) REFERENCES users(email);


--
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

