<h3 class="centered-title">Create new Cloudstead</h3>
<section class="">
	<div class="row centered">
		<form>
			<div class="row field-row">
				<div class="small-2 columns">Name</div>
				<div class="small-4 columns end">{{input type="text" value=name size="50"}}</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">Launch Config</div>
				<div class="small-4 columns">
					{{view Ember.Select
									content=allLaunchConfigs
									selectionBinding="launchConfig"
								}}
				</div>
				<div class="small-6 columns">
					<button {{action 'doNewConfig'}} class="button-small">New Config</button>
				</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">Cloud Config</div>
				<div class="small-4 columns">
					{{view Ember.Select
									content=allClouds
									selectionBinding="cloud"
								}}
				</div>
				<div class="small-6 columns">
					<button {{action 'doNewCloud'}} class="button-small">New Cloud</button>
				</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">Region</div>
				<div class="small-4 columns end">
					{{view Ember.Select
									content=allRegions
									selectionBinding="region"
								}}
				</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">Instance Types</div>
				<div class="small-4 columns end">
					{{view Ember.Select
									content=allInstanceTypes
									selectionBinding="instanceType"
								}}
				</div>
			</div>
			<div class="row field-row">
				<div class="small-2 columns">SSH Key</div>
				<div class="small-4 columns">
					{{view Ember.Select
									content=allSSHKeys
									selectionBinding="sshKey"
								}}
				</div>
				<div class="small-6 columns">
				<ul class="stack-for-small button-group even-2">
					<li><button {{action 'doNewSSH'}} class="button-small">New SSH Key</button></li>
					<li><button {{action 'doManageSSH'}} class="button-small">Manage SSH Keys</button></li>
				</ul>
				</div>
			</div>
			<div class="row field-row">
				<div class="button-group even-2">
					<button {{action 'doLaunch'}} class="button-small">Launch</button>
					<button {{action 'doCancel'}} class="button-small">Cancel</button>
				</div>
			</div>
		</form>
	</div>
</section>
