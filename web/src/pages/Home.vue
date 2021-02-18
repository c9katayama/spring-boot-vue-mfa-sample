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
	MFA登録後、ログアウトしてください。再ログイン時にOTPコード入力が必要となります。
</b-container>
</template>