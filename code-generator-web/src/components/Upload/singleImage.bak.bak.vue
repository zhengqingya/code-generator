<template>
  <div class="upload-container">
    <el-upload
      :data="dataObj"
      :multiple="false"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :on-success="handleImageSuccess"
      class="image-uploader"
      drag
      :action="upload_qiniu_url">
      <i class="el-icon-upload"/>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
    </el-upload>
    <div class="image-preview">
      <div v-show="imageUrl.length>1" class="image-preview-wrapper">
        <img :src="imageUrl+'?imageView2/1/w/200/h/200'">
        <div class="image-preview-action">
          <i class="el-icon-delete" @click="rmImage"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  // 预览效果见付费文章
  import {getToken} from '@/api/qiniu'

  export default {
    name: 'SingleImageUpload',
    props: {
      value: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        tempUrl: '',
        dataObj: {token: '', key: ''},
        // 七牛云上传储存区域的上传域名（华东、华北、华南、北美、东南亚）
        upload_qiniu_url: "http://upload-z1.qiniup.com",
        // 七牛云返回储存图片的子域名
        upload_qiniu_addr: "http://pmr7cm9f6.bkt.clouddn.com/",
      }
    },
    computed: {
      imageUrl() {
        return this.value
      }
    },
    created() {
      this.getToken();
    },
    methods: {
      rmImage() {
        this.emitInput('')
      },
      emitInput(val) {
        this.$emit('input', val)
      },
      handleImageSuccess(res, file, fileList) {
        this.tempUrl = this.upload_qiniu_addr + res.key;
        this.emitInput(this.tempUrl);
      },
      beforeUpload(file) {
        const _self = this
        _self._data.dataObj.key = file.name
      },
      getToken(){
        const _self = this;
        getToken().then(response => {
          const token = response.data
          _self._data.dataObj.token = token
        }).catch(err => {
          console.log(err)
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/mixin.scss";

  .upload-container {
    width: 100%;
    position: relative;
    @include clearfix;
    .image-uploader {
      width: 60%;
      float: left;
    }
    .image-preview {
      width: 200px;
      height: 200px;
      position: relative;
      border: 1px dashed #d9d9d9;
      float: left;
      margin-left: 50px;
      .image-preview-wrapper {
        position: relative;
        width: 100%;
        height: 100%;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .image-preview-action {
        position: absolute;
        width: 100%;
        height: 100%;
        left: 0;
        top: 0;
        cursor: default;
        text-align: center;
        color: #fff;
        opacity: 0;
        font-size: 20px;
        background-color: rgba(0, 0, 0, .5);
        transition: opacity .3s;
        cursor: pointer;
        text-align: center;
        line-height: 200px;
        .el-icon-delete {
          font-size: 36px;
        }
      }
      &:hover {
        .image-preview-action {
          opacity: 1;
        }
      }
    }
  }

</style>
