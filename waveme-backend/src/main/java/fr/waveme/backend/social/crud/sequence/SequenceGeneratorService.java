package fr.waveme.backend.social.crud.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

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
