/**
 * Represents the entity for a word stored in the database. This class is used by Room to create
 * the table structure for stored words, including their definitions and the date they were saved.
 * Each instance of this class represents a row in the "words" table.
 *
 * @author Jungmin Ju
 * @labSection CST2335 011
 * @creationDate 2024-03-29
 */
package algonquin.cst2335.ju000013;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class WordEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "definition")
    public String definition;

    @ColumnInfo(name = "saved_date")
    public long savedDate;

    /**
     * Constructs a new WordEntity with the specified word, definition, and saved date.
     *
     * @param word The word to be stored.
     * @param definition The definition of the word.
     * @param savedDate The timestamp when the word was saved.
     */
    public WordEntity(String word, String definition, long savedDate) {
        this.word = word;
        this.definition = definition;
        this.savedDate = savedDate;
    }
}
