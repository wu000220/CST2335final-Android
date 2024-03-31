/**
 * WordDao is an interface for the Room persistence library, defining methods to access the application's
 * database. It provides the necessary DAO (Data Access Object) methods for inserting, retrieving, and
 * deleting word entities in the database.
 *
 * @author Jungmin Ju
 * @labSection CST2335 011
 * @creationDate 2024-03-29
 */
package algonquin.cst2335.ju000013;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    /**
     * Inserts a new word entity into the database.
     * @param word The WordEntity object to be inserted.
     * @return The row ID of the inserted word entity.
     */
    @Insert
    long insertWord(WordEntity word);

    /**
     * Retrieves all word entities from the database.
     * @return A list of WordEntity objects.
     */
    @Query("SELECT * FROM words")
    List<WordEntity> getAllWords();

    /**
     * Deletes specific word entities from the database based on their IDs.
     * @param ids A list of IDs corresponding to the word entities to be deleted.
     */
    @Query("DELETE FROM words WHERE id IN (:ids)")
    void deleteWordsByIds(List<Long> ids);
}
