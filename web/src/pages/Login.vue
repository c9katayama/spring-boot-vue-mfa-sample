<template>
<b-container>
  <b-navbar toggleable="lg" type="dark" variant="info">
    <b-navbar-brand href="#">MFAサンプル</b-navbar-brand>
  </b-navbar>
  <b-form>
    <b-form-group label="ユーザー名:">
      <b-form-input type="text" id="userName" v-model="userName" required></b-form-input>
    </b-form-group>
    <b-form-group label="パスワード:">
      <b-form-input type="password" id="password" v-model="password" required></b-form-input>
    </b-form-group>    
    <b-form-group label="MFAコード:" v-if="mfaOtpCodeRequired">
      <b-form-input type="text" id="mfaOtpCode" v-model="mfaOtpCode"></b-form-input>
    </b-form-group>
    <b-alert show variant="warning" v-if="errorMessage">{{errorMessage}}</b-alert>
    <b-form-group>
      <b-button variant="outline-primary" type="button" @click="login()">送信</b-button>
    </b-form-group>
  </b-form>
</b-container>
</template>

<script>
import axios from '../api-client.js';
export default {
  data() {
    return {
      userName: "user1",
      password: "password",
      mfaOtpCode: "",
      mfaOtpCodeRequired:false,
      errorMessage: ""
    }
  },
  methods: {
	login() {
		axios.post(
			'/auth/login',
			{
				userName: this.userName,
				password: this.password,
				mfaOtpCode: this.mfaOtpCode
			}
		).then((response) => {
			console.log(response)
			this.$router.push({
				name: 'home',
			})
		}).catch(err => {
			console.log('err:', err);
			this.errorMessage=err.response.data.errorMessage;
			if(err.response.data.errorCode=="E001"){//MFA otp code required
				this.mfaOtpCodeRequired=true;
			}
		});
		this.errorMessage="";
    }
  }
}
</script>