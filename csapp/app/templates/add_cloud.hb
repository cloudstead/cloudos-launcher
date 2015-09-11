<h3 class="centered-title">Create new Cloud</h3>
<section class="">
	<div class="row centered">
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
				<button {{action 'doCreate'}} class="button-smalls">Create</button>
				<button {{action 'doCancel'}} class="button-small">Cancel</button>
			</div>
		</form>
	</div>
</section>
