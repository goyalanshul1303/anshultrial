/*@DataMongoTest
@RunWith(SpringRunner.class)*/
public class MongoDbSpringIntegrationTest {
   
    /*@Test
    public void test(@Autowired MongoTemplate mongoTemplate) {
        // given
        DBObject objectToSave = BasicDBObjectBuilder.start()
            .add("key", "value")
            .get();
 
        // when
        mongoTemplate.save(objectToSave, "collection");
 
        // then
        assertThat(mongoTemplate.findAll(DBObject.class, "collection")).extracting("key")
            .containsOnly("value");
    }*/
}