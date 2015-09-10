<h3 class="centered-title">Create new Cloud</h3>
<section class="deck">
	<div class="row window_box small-12 medium-8 medium-offset-2 large-4 large-offset-4 columns">
		<form>
			<div class="row">
				Name: {{input type="text" value=name size="50"}}
			</div>
			<div class="row">
				Vendor: {{view Ember.Select
									content=allVendors
									selectionBinding="vendor"
								}}
			</div>
			<div class="row">
				Access Key: {{input type="password" value=accessKey size="50"}}
			</div>
			<div class="row">
				Secret Key: {{input type="password" value=secretKey size="50"}}
			</div>
			<div class="row">
				<button {{action 'doCreate'}} class="small-6 small-offset-3 medium-4 medium-offset-4 large-3 large-offset-4 columns">Create</button>
			</div>
		</form>
	</div>
</section>
