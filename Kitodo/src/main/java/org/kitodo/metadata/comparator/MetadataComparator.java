/*
 * (c) Kitodo. Key to digital objects e. V. <contact@kitodo.org>
 *
 * This file is part of the Kitodo project.
 *
 * It is licensed under GNU General Public License version 3 or later.
 *
 * For the full copyright and license information, please read the
 * GPL3-License.txt file that was distributed with this source code.
 */

package org.kitodo.metadata.comparator;

import java.util.Comparator;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kitodo.api.ugh.MetadataInterface;
import org.kitodo.api.ugh.MetadataTypeInterface;

public class MetadataComparator implements Comparator<MetadataInterface> {
    private static final Logger logger = LogManager.getLogger(MetadataComparator.class);
    private String language;

    public MetadataComparator(String inLanguage) {
        this.language = inLanguage;
    }

    @Override
    public int compare(MetadataInterface firstMetadata, MetadataInterface secondMetadata) {
        if (Objects.isNull(firstMetadata)) {
            return -1;
        }
        if (Objects.isNull(secondMetadata)) {
            return 1;
        }
        String firstName;
        String secondName;
        try {
            MetadataTypeInterface firstMetadataType = firstMetadata.getMetadataType();
            MetadataTypeInterface secondMetadataType = secondMetadata.getMetadataType();
            firstName = firstMetadataType.getNameByLanguage(this.language);
            secondName = secondMetadataType.getNameByLanguage(this.language);
        } catch (NullPointerException e) {
            logger.debug("Language {} for metadata {} or {} is missing in ruleset", this.language,
                    firstMetadata.getMetadataType(), secondMetadata.getMetadataType());
            return 0;
        }
        if (Objects.isNull(firstName) || firstName.isEmpty()) {
            firstName = firstMetadata.getMetadataType().getName();
            if (Objects.isNull(firstName)) {
                return -1;
            }
        }
        if (Objects.isNull(secondName) || secondName.isEmpty()) {
            secondName = secondMetadata.getMetadataType().getName();
            if (Objects.isNull(secondName)) {
                return 1;
            }
        }

        return firstName.compareToIgnoreCase(secondName);
    }
}
