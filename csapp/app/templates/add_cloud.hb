<h3 class="centered-title">
	{{#if isEdit }}{{t add_cloud.title_update}}{{else}}{{t add_cloud.title_create}}{{/if}}
</h3>
<section class="">
	<div class="row centered">
		<form>
			<div class="row">
				{{t add_cloud.cloud_name}}: {{input type="text" value=name size="50"}}
			</div>
			<div class="row">
				{{t add_cloud.cloud_provider}}: {{view Ember.Select
									content=availableVendors
									selectionBinding="vendor"
								}}
			</div>
			<div class="row">
				{{t add_cloud.access_key}}: {{input type="password" value=accessKey size="50"}}
			</div>
			<div class="row">
				{{t add_cloud.secret_key}}: {{input type="password" value=secretKey size="50"}}
			</div>
			<div class="row">
				<button {{action 'doUpdate'}} class="button-smalls">
					{{#if isEdit }}{{t add_cloud.update}}{{else}}{{t add_cloud.create}}{{/if}}
				</button>

				<button {{action 'doCancel'}} class="button-small">{{t add_cloud.cancel}}</button>
			</div>
		</form>
	</div>
</section>
