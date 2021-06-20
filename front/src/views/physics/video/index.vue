<template>
  <div class="search">
    <Raw >
      <Col span="12">
        <video id="myVideo" class="video-js" style="height:300px;width:100%;">
            <source src="https://artskyhome.com:8082/bs/video/v1.mp4" type="video/mp4">
        </video>
      </Col>
      <Col span="12">
        <video id="myVideo1" class="video-js" style="height:300px;width:100%;">
            <source src="https://artskyhome.com:8082/bs/video/v2.mp4" type="video/mp4">
        </video>
      </Col>
    </Raw>
    <Raw >
      <Col span="12">
        <video id="myVideo2" class="video-js" style="height:300px;width:100%;">
            <source src="https://artskyhome.com:8082/bs/video/v3.mp4" type="video/mp4">
        </video>
      </Col>
      <Col span="12">
        <video id="myVideo3" class="video-js" style="height:300px;width:100%;">
            <source src="https://artskyhome.com:8082/bs/video/v4.mp4" type="video/mp4">
        </video>
      </Col>
    </Raw>
  </div>
</template>

<script>
export default {
  name: "single-window",
  components: {
  },
  data() {
    return {
    };
  },
  methods: {
    init() {
      this.initVideo();
    },
    initVideo() {
      let myPlayer = this.$video(myVideo, {
          //确定播放器是否具有用户可以与之交互的控件。没有控件，启动视频播放的唯一方法是使用autoplay属性或通过Player API。
          controls: true,
          //自动播放属性,muted:静音播放
          autoplay: "muted",
          //建议浏览器是否应在<video>加载元素后立即开始下载视频数据。
          preload: "auto",
          width: "800px",
          height: "200px"
      });
      let myPlayer1 = this.$video(myVideo1, {
          controls: true,
          autoplay: "muted",
          preload: "auto",
          width: "800px",
          height: "200px"
      });
      let myPlayer2 = this.$video(myVideo2, {
          controls: true,
          autoplay: "muted",
          preload: "auto",
          width: "800px",
          height: "200px"
      });
      let myPlayer3 = this.$video(myVideo3, {
          controls: true,
          autoplay: "muted",
          preload: "auto",
          width: "800px",
          height: "200px"
      });
    },
  },
  mounted() {
    this.init();
  }
};
</script>
<style lang="less">
// 建议引入通用样式 具体路径自行修改 可删除下面样式代码
// @import "../../../styles/table-common.less";
.search {
    .operation {
        margin-bottom: 2vh;
    }
    .select-count {
        font-weight: 600;
        color: #40a9ff;
    }
    .select-clear {
        margin-left: 10px;
    }
    .page {
        margin-top: 2vh;
    }
    .drop-down {
        margin-left: 5px;
    }
}
</style>