<template>
<b-container>
  <h2>ログアウト中・・</h2>
</b-container>
</template>

<script>
import axios from '../api-client.js';
export default {
	methods: {
		logout(){  
			axios.post(
				'/auth/logout',{}
			).then((response) => {
				console.log(response);
				this.$router.push({
					name: 'login',
				})
			}).catch(err => {
				console.log('err:', err);
			});
		}
	},
	mounted(){
		this.logout();
	}
}
</script>