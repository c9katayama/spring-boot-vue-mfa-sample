<template>
<b-container>
	<b-navbar toggleable="lg" type="dark" variant="info">
		<b-navbar-brand href="#">MFAサンプル</b-navbar-brand>
		<b-collapse id="nav-collapse" is-nav>
			<b-navbar-nav>
				<router-link :to="{ name: 'register-mfa'}" class="nav-item nav-link">MFA登録</router-link>
				<router-link :to="{ name: 'logout'}" class="nav-item nav-link">ログアウト</router-link>
			</b-navbar-nav>
		</b-collapse>
	</b-navbar>
	
	<b-alert show variant="warning" v-if="errorMessage">{{errorMessage}} データを初期化するには、SpringBootを再起動してください。</b-alert>
	
	<b-card v-if="!errorMessage" header="MFA登録">
		<div>
			<qriously :value="totpAuthUri" :size="200" v-if="totpAuthUri"/>
		</div>
		<div>
			QRを多要素認証アプリ（Google Authenticator)などで読み取り、表示される六桁のコードを入力して有効化してください。
		</div>
		<b-form>
			<b-form-group label="MFAコード:">
				<b-form-input type="text" id="mfaOtpCode" v-model="mfaOtpCode"></b-form-input>
			</b-form-group>
			<b-alert show variant="warning" v-if="mfaCodeErrorMessage">{{mfaCodeErrorMessage}}</b-alert>
			<b-form-group>
				<b-button variant="outline-primary" type="button" @click="activateMFA()">有効化</b-button>
			</b-form-group>
			<b-alert show variant="success" v-if="mfaEnabledMessage">{{mfaEnabledMessage}}</b-alert>
		</b-form>
    </b-card>
	
</b-container>
</template>

<script>
import axios from '../api-client.js';
export default {
	data() {
		return {
			totpAuthUri:"",
			mfaOtpCode:"",
			errorMessage: "",
			mfaCodeErrorMessage:"",
			mfaEnabledMessage:"",
		}
	},
	methods: {
		initMFA() {  
			axios.post(
				'/auth/mfa/init',{}
			).then((response) => {
				console.log(response.data);
				this.totpAuthUri=response.data.totpAuthUri;
			}).catch(err => {
				console.log('err:', err);
				this.errorMessage=err.response.data.errorMessage;
			});
			this.errorMessage="";
		},
		activateMFA(){
			axios.post(
				'/auth/mfa/activate',{
					mfaOtpCode:this.mfaOtpCode
				}
			).then((response) => {
				console.log(response.data);
				this.mfaEnabledMessage="MFAが有効化されました。ログアウトして再度ログインしてください。";
			}).catch(err => {
				console.log(err.response.data);
				this.mfaCodeErrorMessage=err.response.data.errorMessage;
			});
			this.errorMessage="";
			this.mfaCodeErrorMessage="";
		}
	},
	mounted(){
		this.initMFA();
	}  
}
</script>