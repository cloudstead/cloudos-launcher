<h3 class="centered-title">Create new Cloud</h3>
<section class="">
	<div class="row centered">
		<form>
			<div class="row">
				Name: {{input type="text" value=name size="50"}}
			</div>
			<div class="row">
				Vendor: {{view Ember.Select
									content=availableVendors
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
				<button {{action 'doUpdate'}} class="button-smalls">
					{{#if isEdit }}Update{{else}}Create{{/if}}
				</button>

				<button {{action 'doCancel'}} class="button-small">Cancel</button>
			</div>
		</form>
	</div>
</section>
