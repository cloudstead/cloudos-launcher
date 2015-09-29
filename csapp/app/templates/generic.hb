<ul>
{{!-- {{#generic_template model}}{{/generic_template}} --}}
	<div class='wiz_panel'>
		<h3 class='wiz_panel_header'>{{title}}</h3>
		<p class='wiz_desc'>{{description}}</p>
		<div class='row wiz_no_margin'>
			{{#each field in fields }}
				<div class='large-5 columns wiz_dns_tab'>
					{{#if field.isChoice}}
						<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
						{{field.label}}
						{{view Ember.Select
								content=field.choices
								optionValuePath="content.value"
								optionLabelPath="content.label"
								valueBinding=field.value
								data-element-id=field.elementId }}
						</label>
					{{else}}

					{{#if field.isCronSchedule}}
						<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
							{{field.label}}
							{{view Ember.Select
								content=field.cronChoices
								optionValuePath="content.value"
								optionLabelPath="content.label"
								valueBinding=field.value
								data-element-id=field.elementId }}
						</label>
						<script>
							$('.time_of_day').timepicker({ 'scrollDefault': 'now' });
						</script>
						{{else}}
							<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
								{{field.label}}
								{{ input
									type=field.type.typeName
									value=field.value
									title=field.info
									class="large-5 columns wiz_dns_tab"
									data-element-id=field.elementId
									data-kind=field.type.typeKind
									data-required=field.required
								}}
							</label>
						{{/if}}
					{{/if}}
				</div>
			{{/each}}
			{{#each fileField in fileFields }}
				<div class='large-5 columns wiz_dns_tab'>
					<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
						{{fileField.label}}
						{{ input
							type=fileField.type.typeName
							value=fileField.value
							title=fileField.help
							class="large-5 columns wiz_dns_tab"
							data-element-id=fileField.elementId
						}}
					</label>
				</div>
			{{/each}}
			<div class='clear_fix'></div>
			{{#each subTabGroup in subTabGroups}}
				<h3 class='wiz_panel_header'>{{subTabGroup.tabGroupCaption}}</h3>
				<p class='wiz_desc'>{{subTabGroup.tabGroupHelp}}</p>
				<div id='tab-container'>
					<ul class='tab-menu'>
						{{#each tab in subTabGroup.tabs}}
							<li {{bind-attr id=tab.tabName}} {{action "activateTab" tab}} {{bind-attr class="tab.isActive:active"}}>{{tab.tabTitle}}</li>
						{{/each}}
					</ul>
					<div class='clear_fix'></div>
					<div class='tab-top-border'></div>
						{{#each tab in subTabGroup.tabs}}
								<div {{bind-attr id=tab.tabContentId}} {{bind-attr class="tab.isActive:tab-content-block:tab-content-hidden"}}>
								<p class='wiz_desc'>{{tab.tabDescription}}</p>

								{{#each field in tab.tabFields }}
									<div class='large-5 columns wiz_dns_tab'>
										{{#if field.isChoice}}
											<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
											{{field.label}}
											{{view Ember.Select
													content=field.choices
													optionValuePath="content.value"
													optionLabelPath="content.label"
													data-element-id=field.elementId }}
											</label>
										{{else}}

										{{#if field.isTimeOfDay}}
											<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
												{{field.label}}
												{{ input
													type=field.type.typeName
													value=field.value
													title=field.info
													class="large-5 columns wiz_dns_tab time_of_day"
													data-element-id=field.elementId
												}}
											</label>
											<script>
												$('.time_of_day').timepicker({ 'scrollDefault': 'now' });
											</script>
											{{else}}
												<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
													{{field.label}}
													{{ input
														type=field.type.typeName
														value=field.value
														title=field.info
														class="large-5 columns wiz_dns_tab"
														data-element-id=field.elementId
														data-kindtab=field.type.typeKind
														data-required=field.required
													}}
												</label>
											{{/if}}
										{{/if}}
									</div>
								{{/each}}
								{{#each fileField in tab.tabFileFields }}
									<div class='large-5 columns wiz_dns_tab'>
										<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
											{{fileField.label}}
											{{ input
												type=fileField.type.typeName
												value=fileField.value
												title=fileField.help
												class="large-5 columns wiz_dns_tab"
												data-element-id=fileField.elementId
											}}
										</label>
									</div>
								{{/each}}
								<div class='clear_fix'></div>
							</div>

						{{/each}}
					</div>
			{{/each}}
		</div>
	</div>
</ul>
