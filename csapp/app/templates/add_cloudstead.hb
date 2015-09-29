<h3 class="centered-title">
	{{#if isEdit }}{{t add_cloudstead.title_update}}{{else}}{{t add_cloudstead.title_create}}{{/if}}
</h3>
<section class="">
	<div class="row centered">
		<form>
			<div class="row field-row">
				<div class="small-2 columns">{{t add_cloudstead.cloudstead_name}}</div>
				<div class="small-4 columns end">{{input type="text" value=name size="50"}}</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">{{t add_cloudstead.launch_config}}</div>
				<div class="small-4 columns">
					{{view Ember.Select
									content=allLaunchConfigs
									selectionBinding="launchConfig"
								}}
				</div>
				<div class="small-6 columns">
					<button {{action 'doNewConfig'}} class="button-small">{{t add_cloudstead.new_config}}</button>
				</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">{{t add_cloudstead.cloud_config}}</div>
				<div class="small-4 columns">
					{{view Ember.Select
									content=allClouds
									selectionBinding="cloud"
								}}
				</div>
				<div class="small-6 columns">
					<button {{action 'doNewCloud'}} class="button-small">
						{{t add_cloudstead.new_cloud}}
					</button>
				</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">{{t add_cloudstead.region}}</div>
				<div class="small-4 columns end">
					{{view Ember.Select
									content=allRegions
									selectionBinding="region"
								}}
				</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">{{t add_cloudstead.instance_types}}</div>
				<div class="small-4 columns end">
					{{view Ember.Select
									content=allInstanceTypes
									selectionBinding="instanceType"
								}}
				</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">{{t add_cloudstead.ssh_key}}</div>
				<div class="small-4 columns">
					{{view Ember.Select
									content=allSSHKeys
									selectionBinding="sshKey"
								}}
				</div>
				<div class="small-6 columns">
				<ul class="stack-for-small button-group even-2">
					<li>
						<button {{action 'doNewSsh'}} class="button-small">
							{{t add_cloudstead.new_ssh}}
						</button>
					</li>
					<li><button {{action 'doManageSsh'}} class="button-small">
						{{t add_cloudstead.manage_ssh}}
					</button></li>
				</ul>
				</div>
			</div>
			<div class="row field-row">
				<div class="button-group even-2">
					<button {{action 'doLaunch'}} class="button-small">
						{{#if isEdit }}{{t add_cloudstead.update}}{{else}}{{t add_cloudstead.launch}}{{/if}}
					</button>
					<button {{action 'doCancel'}} class="button-small">
						{{t add_cloudstead.cancel}}
					</button>
				</div>
			</div>
		</form>
	</div>
</section>
