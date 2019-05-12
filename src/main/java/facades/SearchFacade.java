package facades;

import entities.LAEvaluation;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class SearchFacade {

    @PersistenceContext
    EntityManager em;

    public List<LAEvaluation> findAll(JSONObject params) {
        String sql = "select e from LAEvaluation e";

        List<String> clauses = new LinkedList<>();
        if(params.has("epic")) {
            clauses.add("e.star.epic=" + params.getString("epic"));
        }
        if(params.has("username")) {
            if(params.getString("username").contains("#")) {
                clauses.add("'#' || e.user.id='" + params.getString("username") + "'");
            } else {
                clauses.add("lower(e.user.username)=lower('" + params.getString("username") + "')");
            }
         }
         if(params.has("classification")) {
            clauses.add("e.classificationResult>=" + params.getString("classification"));
         }
         if(params.has("candidateCnt")) {
            clauses.add("(select count(c) from LACandidate c where c.evaluation.id=e.id)=" + params.getString("candidateCnt"));
         }

         String sqlWhere = String.join(" and ", clauses);

        if(!sqlWhere.isEmpty()) {
            sql += (" where " + sqlWhere);
        }

        if (params.getString("sort").compareToIgnoreCase("TIMESTAMP") == 0) {
            sql += " order by e.creationTimeStamp desc";
        } else if(params.getString("sort").compareToIgnoreCase("EPIC NUMBER") == 0) {
            sql += " order by e.star.epic";
        } else if(params.getString("sort").compareToIgnoreCase("USERNAME") == 0) {
            sql += " order by e.user.username";
        } else if(params.getString("sort").compareToIgnoreCase("CANDIDATE NUMBER") == 0) {
            sql += " order by (select count(c) from LACandidate c where c.evaluation.id=e.id)";
        }

        List<LAEvaluation> evals = em.createQuery(sql).getResultList();
        return evals;
    }
}
