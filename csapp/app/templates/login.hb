<h3 class="centered-title">{{t login.title}}</h3>
<section class="deck">
	<div class="row window_box small-12 medium-8 medium-offset-2 large-4 large-offset-4 columns">
		<form>
			<div class="row">
				{{t login.username}}: {{input type="text" value=username size="50"}}
			</div>
			<div class="row">
				{{t login.password}}: {{input type="password" value=password size="50"}}
			</div>
			<div class="row">
				<button {{action 'odLogin'}} class="small-6 small-offset-3 medium-6 medium-offset-3 large-4 large-offset-4 columns">{{t login.button_caption}}</button>
			</div>
		</form>
	</div>
</section>
