<template>
  <div class="main">
    <div style="line-height:24px;padding: 10px;font-size: 16px;text-align:center;font-weight:bolder;">
      账号密码登录
    </div>
    <a-form
      id="formLogin"
      class="user-layout-login"
      ref="formLogin"
      :form="form"
    >
      <a-alert v-if="isLoginError" type="error" showIcon style="margin-bottom: 24px;" :message="errorMsg" />
      <a-form-item>
        <a-input
          size="large"
          type="text"
          placeholder="请输入账号"
          v-decorator="[
            'account',
            {rules: [{ required: true, message: '请输入登录账号' }], validateTrigger: 'change'}
          ]"
        >
          <span slot="suffix" class="login-input-icon"><a-icon type="user" /></span>
        </a-input>
      </a-form-item>

      <a-form-item>
        <a-input
          size="large"
          :type="pwdVisible ? 'text' : 'password'"
          autocomplete="false"
          placeholder="请输入密码"
          v-decorator="[
            'pwd',
            {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}
          ]"
        >
          <span slot="suffix" class="login-input-icon" @click="chagnePwdVisible">
            <a-icon :type="pwdVisible ? 'eye-invisible' : 'eye'" />
          </span>
        </a-input>
      </a-form-item>

      <a-form-item>
        <a-input 
          size="large" 
          type="text" 
          placeholder="请输入验证码"
          v-decorator="[
            'captcha',
            {rules: [{ required: true, message: '请输入验证码' }], validateTrigger: 'blur'}
          ]"
          >
          <span slot="suffix" class="login-input-icon"><a-icon type="code" /></span>
        </a-input>
      </a-form-item>

      <a-form-item style="margin-top:24px">
        <a-row>
          <a-col :span="16">
            <img v-if="validImg" :src="validImg" @click="generateRandomImage" 
              title="点击更换图片"
              style="height: 35px;width: 105px;cursor: pointer;">
            <a-button v-else loading>加载中...</a-button>
          </a-col>
          <a-col :span="6" :offset="2" style="text-align:right;">
            <a-button
              type="primary"
              htmlType="submit"
              icon="login"
              class="login-button"
              :loading="state.loginBtn"
              :disabled="state.loginBtn"
              @click.stop.prevent="handleSubmit"
            >登录</a-button>
          </a-col>
        </a-row>
      </a-form-item>

    </a-form>

    <two-step-captcha
      v-if="requiredTwoStepCaptcha"
      :visible="stepCaptchaVisible"
      @success="stepCaptchaSuccess"
      @cancel="stepCaptchaCancel"
    ></two-step-captcha>
  </div>
</template>

<script>
import md5 from 'md5'
import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'
import { getSmsCaptcha, get2step, login } from '@/api/login'
import {getAction, postAction, getFile} from '@/api/manage'

export default {
  components: {
    TwoStepCaptcha
  },
  data () {
    return {
      customActiveKey: 'tab1',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      isLoginError: false,
      requiredTwoStepCaptcha: false,
      stepCaptchaVisible: false,
      form: this.$form.createForm(this),
      state: {
        time: 60,
        loginBtn: false,
        // login type: 0 email, 1 username, 2 telephone
        loginType: 0,
        smsSendBtn: false
      },
      loginKey: null,
      validImg: null,
      // 密码是否可见
      pwdVisible: false,
      errorMsg: '账号或密码错误',
      url: {
        generateImg: '/auth/generateImg/',
        generateGif: '/auth/generateGif/'
      }
    }
  },
  created () {
    /* get2step({ })
      .then(res => {
        this.requiredTwoStepCaptcha = res.result.stepCode
      })
      .catch(() => {
        this.requiredTwoStepCaptcha = false
      }) */
    // this.requiredTwoStepCaptcha = true
    this.generateRandomImage()
  },
  methods: {
    ...mapActions(['Login']),
    chagnePwdVisible(){
      this.pwdVisible = !this.pwdVisible
    },
    // handler
    handleUsernameOrEmail (rule, value, callback) {
      const { state } = this
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        state.loginType = 0
      } else {
        state.loginType = 1
      }
      callback()
    },
    generateRandomImage(){
      this.loginKey = new Date().getTime()
      // 返回gif图片
      this.generateGif()
    },
    // 生成Png静态图片验证码
    generatePng(){
      getAction(this.url.generateImg + this.loginKey).then(result => {
        if(!result.code){
          this.validImg = result.data
        }else{
          this.$message.error(result.msg)
        }
      })
    },
    // 生成Gif动态图片验证码
    generateGif(){
      getFile(this.url.generateGif + this.loginKey).then(result => {
        const src = window.URL.createObjectURL(result.data)
        this.validImg = src
      })
    },
    handleTabClick (key) {
      this.customActiveKey = key
      // this.form.resetFields()
    },
    handleSubmit (e) {
      const {
        form: { validateFields },
        state,
        customActiveKey,
        Login
      } = this

      state.loginBtn = true

      const validateFieldsKey = customActiveKey === 'tab1' ? ['account', 'pwd', 'captcha'] : ['mobile', 'captcha']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          const loginParams = { ...values }
          //delete loginParams.username
          //loginParams[!state.loginType ? 'email' : 'username'] = values.username
          loginParams.pwd = md5(values.pwd)
          loginParams.key = this.loginKey

          Login(loginParams)
            .then((res) => {
              if(!res.code){
                this.loginSuccess(res)
              }else{
                // this.$message.error(res.msg)
              }
            })
            .catch(err => this.requestFailed(err))
            .finally(() => {
              state.loginBtn = false
            })
        } else {
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
      })
    },
    getCaptcha (e) {
      e.preventDefault()
      const { form: { validateFields }, state } = this

      validateFields(['mobile'], { force: true }, (err, values) => {
        if (!err) {
          state.smsSendBtn = true

          const interval = window.setInterval(() => {
            if (state.time-- <= 0) {
              state.time = 60
              state.smsSendBtn = false
              window.clearInterval(interval)
            }
          }, 1000)

          const hide = this.$message.loading('验证码发送中..', 0)
          getSmsCaptcha({ mobile: values.mobile }).then(res => {
            setTimeout(hide, 2500)
            this.$notification['success']({
              message: '提示',
              description: '验证码获取成功，您的验证码为：' + res.result.captcha,
              duration: 8
            })
          }).catch(err => {
            setTimeout(hide, 1)
            clearInterval(interval)
            state.time = 60
            state.smsSendBtn = false
            this.requestFailed(err)
          })
        }
      })
    },
    stepCaptchaSuccess () {
      this.loginSuccess()
    },
    stepCaptchaCancel () {
      this.Logout().then(() => {
        this.loginBtn = false
        this.stepCaptchaVisible = false
      })
    },
    loginSuccess (res) {
      // check res.homePage define, set $router.push name res.homePage
      // Why not enter onComplete
      /*
      this.$router.push({ name: 'analysis' }, () => {
        console.log('onComplete')
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      })
      */
      this.$router.push({ path: '/' })
      // 延迟 1 秒显示欢迎信息
      setTimeout(() => {
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      }, 1000)
      this.isLoginError = false
    },
    requestFailed (err) {
      this.generateRandomImage()
      this.errorMsg = err.msg || '请求出现错误，请稍后再试'
      this.isLoginError = true
      /* this.$notification['error']({
        message: '错误',
        description: err.msg || '请求出现错误，请稍后再试',
        duration: 4
      }) */
    }
  }
}
</script>

<style lang="less" scoped>
.main{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  opacity: 0.9;
  padding: 20px;
  border-radius: 5px;

  .login-input-icon{
    cursor: pointer;
    width: 34px;
    height: 34px;
    border-radius: 50%;
    text-align: center;
    padding-top:6.5px;
  }
  .login-input-icon:hover{
    font-size: 16px;
    padding-top:5px;
    background-color: #ccc;
  }
}
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    width: 100%;
    height: 38px;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}
</style>
