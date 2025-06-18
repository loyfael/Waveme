package fr.waveme.backend.social.crud.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


/**
 * This service is responsible for generating unique sequences for entities in the MongoDB database.
 * It uses a MongoDB collection to keep track of the current sequence number for each entity type.
 * The `generateSequence` method increments the sequence number for a given sequence name
 * and returns the new sequence value.
 * Usage:
 * To use this service, inject it into your component and call the `generateSequence` method
 * with the desired sequence name. The method will return the next sequence number,
 * which can be used as a unique identifier for new entities.
 */
@Service
public class SequenceGeneratorService {

  @Autowired
  private MongoTemplate mongoTemplate;

  public long generateSequence(String seqName) {
    Query query = new Query(Criteria.where("_id").is(seqName));
    Update update = new Update().inc("seq", 1);
    FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

    DatabaseSequence counter = mongoTemplate.findAndModify(
            query, update, options, DatabaseSequence.class);

    return counter != null ? counter.getSeq() : 1;
  }
}
