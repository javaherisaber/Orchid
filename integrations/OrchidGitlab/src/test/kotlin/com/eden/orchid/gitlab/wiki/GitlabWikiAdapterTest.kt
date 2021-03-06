package com.eden.orchid.gitlab.wiki

import com.eden.orchid.gitlab.GitlabModule
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.asHtml
import com.eden.orchid.testhelpers.outerHtml
import com.eden.orchid.testhelpers.pageWasRendered
import com.eden.orchid.testhelpers.pagesGenerated
import com.eden.orchid.testhelpers.select
import com.eden.orchid.wiki.WikiModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests page-rendering behavior of Wiki generator")
class GitlabWikiAdapterTest : OrchidIntegrationTest(WikiModule(), GitlabModule()) {

    @Test
    @DisplayName("Wikis can be imported from Gitlab. If no _Sidebar file is present, files will be listed alphabetically.")
    fun test01() {
        configObject(
            "wiki", """
            {
                "sections": {
                    "wiki-without-sidebar": {
                        "adapter": {
                            "type": "gitlab",
                            "repo": "cjbrooks12/wiki-without-sidebar"
                        }
                    }
                }
            }
        """.trimIndent()
        )

        val testResults = execute()

        expectThat(testResults).pagesGenerated(6)
        expectThat(testResults)
            .pageWasRendered("/wiki/wiki-without-sidebar/index.html")
            .get { content }
            .asHtml(removeComments = true)
            .select("body > ul")
            .outerHtml()
            .isEqualTo(
                """
                <ul>
                  <li>
                    <a href="http://orchid.test/wiki/wiki-without-sidebar/Configuration">Configuration</a>
                  </li>
                  <li>
                    <a href="http://orchid.test/wiki/wiki-without-sidebar/GettingStarted">Getting Started</a>
                  </li>
                  <li>
                    <a href="http://orchid.test/wiki/wiki-without-sidebar/Home">Home</a>
                  </li>
                  <li>
                    <a href="http://orchid.test/wiki/wiki-without-sidebar/Installation">Installation</a>
                  </li>
                </ul>
            """.trimIndent()
            )

        expectThat(testResults).pageWasRendered("/wiki/wiki-without-sidebar/Configuration/index.html")
        expectThat(testResults).pageWasRendered("/wiki/wiki-without-sidebar/GettingStarted/index.html")
        expectThat(testResults).pageWasRendered("/wiki/wiki-without-sidebar/Home/index.html")
        expectThat(testResults).pageWasRendered("/wiki/wiki-without-sidebar/Installation/index.html")
    }

    @Test
    @DisplayName("Wikis can be imported from Gitlab. If a _Sidebar file is present, it will be used as the Summary page.")
    fun test02() {
        configObject(
            "wiki", """
            {
                "sections": {
                    "wiki-with-sidebar": {
                        "adapter": {
                            "type": "gitlab",
                            "repo": "cjbrooks12/wiki-with-sidebar"
                        }
                    }
                }
            }
        """.trimIndent()
        )

        val testResults = execute()

        expectThat(testResults).pagesGenerated(6)
        expectThat(testResults)
            .pageWasRendered("/wiki/wiki-with-sidebar/index.html")
            .get { content }
            .asHtml(removeComments = true)
            .select("body > ul")
            .outerHtml()
            .isEqualTo(
                """
                <ul>
                  <li>
                    <a href="http://orchid.test/wiki/wiki-with-sidebar/Home">Home</a>
                  </li>
                  <li>
                    <a href="http://orchid.test/wiki/wiki-with-sidebar/GettingStarted">Getting Started</a>
                    <ul>
                      <li>
                        <a href="http://orchid.test/wiki/wiki-with-sidebar/Installation">Installation</a>
                      </li>
                      <li>
                        <a href="http://orchid.test/wiki/wiki-with-sidebar/Configuration">Configuration</a>
                      </li>
                    </ul>
                  </li>
                </ul>
            """.trimIndent()
            )

        expectThat(testResults).pageWasRendered("/wiki/wiki-with-sidebar/Configuration/index.html")
        expectThat(testResults).pageWasRendered("/wiki/wiki-with-sidebar/GettingStarted/index.html")
        expectThat(testResults).pageWasRendered("/wiki/wiki-with-sidebar/Home/index.html")
        expectThat(testResults).pageWasRendered("/wiki/wiki-with-sidebar/Installation/index.html")
    }

}
