package com.eden.orchid.impl.resources

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.InlineStringResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import java.util.regex.Pattern
import javax.inject.Inject

class InlineResourceSource
@Inject
constructor(
        private val context: OrchidContext
) : LocalResourceSource {

    override val priority: Int = Integer.MAX_VALUE - 1

    override fun getResourceEntry(fileName: String): OrchidResource? {
        val m = inlineFilenamePattern.matcher(fileName)

        if (m.find()) {
            val actualFileName = m.group(2)
            val fileContent = m.group(3)
            return InlineStringResource(context, actualFileName, fileContent)
        }

        return null
    }

    override fun getResourceEntries(dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        return emptyList()
    }

    companion object {
        private val inlineFilenamePattern = Pattern.compile("^(inline:(.*?):)(.*)", Pattern.DOTALL)
    }
}


