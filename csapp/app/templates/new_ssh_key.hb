<h3 class="centered-title">{{t new_ssh.title}}</h3>
<section class="">
	<div class="row centered">
		<form>
			<div class="row">
				{{t new_ssh.key_name}}: {{input type="text" value=name size="50"}}
			</div>
			<div class="row">
				{{t new_ssh.select_file}}
			</div>
			<div class="row">
				{{view App.TextFileUpload action="doReadUploadedFile"}}
			</div>
			<div class="row">
				{{t new_ssh.or}}
			</div>
			<div class="row">
				{{textarea value=publicKey cols="80" rows="6" placeholder="paste key in here"}}
			</div>
			<div class="row">
				<button {{action 'doAddKey'}} class="small-6  medium-4 large-3 columns">
					{{t new_ssh.add_key}}
				</button>
				<button {{action 'doCancel'}} class="small-6 small-offset-1 medium-4 large-3 columns">
					{{t new_ssh.cancel}}
				</button>
			</div>
		</form>
	</div>
</section>
