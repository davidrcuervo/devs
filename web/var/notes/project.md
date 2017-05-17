% Multimedia project slides
% David Cuervo
% March 30, 2017

# Video server
Upload and Stream videos on the web 

By [David Cuervo](#) / [\@davidrcuervo](#)

----

## Try it now

[https://www.la-etienda.com/media/video](https://www.la-etienda.com/media/video)

[https://www.la-etienda.com/notes/project.md?format=slides](https://www.la-etienda.com/notes/project.md?format=slides)


----

# Why a video server

To provide the Ballet Raices De Colombia with an easy and cheap way to share their videos.

----

## The BRDC

The dancing group represents the folklore from Colombia. They exist in Montreal since 16 years ago.

![https://www.la-etienda.com/assets/BRDC1.jpg](https://www.la-etienda.com/assets/BRDC1.jpg)

----

## The main purpose

Videos are very important for the group to promote them. But videos requires high storage room.

![https://www.la-etienda.com/assets/BRDC2.jpg](https://www.la-etienda.com/assets/BRDC2.jpg)

----

## Video servers

Video option like youtube, vimeo, etc. are good options but they offer only few gigas for free, and unwanted advertisment on their streaming solution.


![](https://www.la-etienda.com/assets/youtube.jpg)

![](https://www.la-etienda.com/assets/vimeo.jpg)


----

## The video

Once it is uploaded it compress by using **FFMPEG** to  mp4 and webm to support most of modern web explorer. It does not support flashplayer but who uses flashplayer nowdays?
<br />
<br />
<br />

| Format | video codec | audio codec |
|:------:|:-----------:|:-----------:|
| mp4    | h.264       | acc         |
| webm   | vp8         | vorbis      |

----

## Download it.

You can download the server an try it on your side, it only requires a pc with java and ffmpeg.

<br />
<br />

#### GitHub web site:

[https://github.com/davidrcuervo/devs/tree/web-dev](https://github.com/davidrcuervo/devs/tree/web-dev)

<br />
<br />

#### Clone the project

[https://github.com/davidrcuervo/devs.git](https://github.com/davidrcuervo/devs.git)


----

## Important resources.

* [http://www.adrianwalker.org/2009/09/http-proxy-servlet.html](http://www.adrianwalker.org/2009/09/http-proxy-servlet.html)
* [https://gist.github.com/Vestride/278e13915894821e1d6f](https://gist.github.com/Vestride/278e13915894821e1d6f)
* [http://www.avajava.com/tutorials/lessons/how-do-i-monitor-the-progress-of-a-file-upload-to-a-servlet.html?page=1](http://www.avajava.com/tutorials/lessons/how-do-i-monitor-the-progress-of-a-file-upload-to-a-servlet.html?page=1)
* [https://xmlgraphics.apache.org/batik/using/transcoder.html](https://xmlgraphics.apache.org/batik/using/transcoder.html)

