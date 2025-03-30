package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.User;
import unlp.info.bd2.utils.ToursException;

public interface ToursRepository {
    
    //IVY
    public User saveOrUpdateUser(User user) throws ToursException;

    public Optional<User> getUserById(Long id);

    public Optional<User> getUserByUsername(String username);

    public void deleteUser(Long id) throws ToursException;

    public Stop saveOrUpdateStop(Stop stop);

    public List<Stop> getStopByNameStart(String name);

    //FABRI


    //FRANCO
}
