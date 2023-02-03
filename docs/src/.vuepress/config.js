const {description} = require('../../package.json')

module.exports = {
    base: "/",
    /**
     * Ref：https://v1.vuepress.vuejs.org/config/#title
     */
    title: 'ISI Backend',
    /**
     * Ref：https://v1.vuepress.vuejs.org/config/#description
     */
    description: description,
    /**
     * Output directory
     */
    dest: "dist",

    /**
     * Extra tags to be injected to the page HTML `<head>`
     *
     * ref：https://v1.vuepress.vuejs.org/config/#head
     */
    head: [
        ['meta', {name: 'theme-color', content: '#333333'}],
        ['meta', {name: 'apple-mobile-web-app-capable', content: 'yes'}],
        ['meta', {name: 'apple-mobile-web-app-status-bar-style', content: 'black'}]
    ],
    locales: {
        '/': {
            lang: 'de-DE', // this will be set as the lang attribute on <html>
            title: 'ISI Backend',
            description: ''
        }
        // '/en/': {
        //     lang: 'en-US', // this will be set as the lang attribute on <html>
        //     title: 'ISI',
        //     description: ''
        // }
    },

    /**
     * Theme configuration, here is the default theme configuration for VuePress.
     *
     * ref：https://v1.vuepress.vuejs.org/theme/default-theme-config.html
     */
    themeConfig: {
        repo: 'https://github.com/it-at-m/isi-backend',
        editLinks: false,
        docsDir: '',
        editLinkText: '',
        lastUpdated: false,
        locales: {
            '/': {
                selectText: 'Sprachen',
                label: 'Deutsch',
                ariaLabel: 'Sprachen',
                nav: [
                    {
                        text: 'Zur Dokumentation',
                        link: '/documentation/',
                    }
                ],
                sidebar: {
                    '/documentation/': [
                        {
                            title: 'ISI Backend',
                            collapsable: false,
                            children: [
                                '',
                                'roadmap'
                            ]
                        },
                        {
                            title: 'Guides',
                            collapsable: false,
                            children: [
                                'guides/conventions',
                                'guides/technical-setup/',

                            ]
                        }
                    ],
                }
            },
            '/en/': {
                selectText: 'Languages',
                // label for this locale in the language dropdown
                label: 'English',
                // Aria Label for locale in the dropdown
                ariaLabel: 'Languages',
                nav: [

                ],
                sidebar: {

                }
            }
        },
    },

    /**
     * markdown extension
     */
    markdown: {
        lineNumbers: true,
        extendMarkdown: md => {
            md.use(require('markdown-it-footnote'))
        }
    },

    /**
     * Apply plugins，ref：https://v1.vuepress.vuejs.org/plugin/
     */
    plugins: [
        '@vuepress/plugin-back-to-top',
        '@vuepress/plugin-medium-zoom',
        'vuepress-plugin-mermaidjs'
    ]
}
