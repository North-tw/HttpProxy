module.exports = {
	env: {
		browser: true,
		es2020: true
	},
	extends: [
		"airbnb-base"
	],
	parserOptions: {
		ecmaVersion: 11,
		sourceType: "module"
	},
	rules: {
		"no-tabs": ["error", { allowIndentationTabs: true }],
		indent: ["error", "tab", { SwitchCase: 1 }],
		"comma-dangle": ["error", "never"],
		"no-plusplus": ["error", { allowForLoopAfterthoughts: true }],
		quotes: ["error", "double"],
		"prefer-template": ["off"],
		"no-continue": ["off"],
		"max-len": ["off"],
		"import/extensions": ["error",
			"ignorePackages",
			{
				js: "always",
				jsx: "never",
				ts: "always",
				tsx: "never"
			}]
	}
};
