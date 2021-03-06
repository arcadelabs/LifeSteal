/*
 *          LifeSteal - Yet another lifecore smp core.
 *                Copyright (C) 2022  Arcade Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.database.DatabaseHandler;
import in.arcadelabs.lifesteal.database.querybuilder.CreateTableQuery;
import in.arcadelabs.lifesteal.database.querybuilder.InsertQuery;
import in.arcadelabs.lifesteal.database.querybuilder.Query;
import in.arcadelabs.lifesteal.database.querybuilder.SelectQuery;
import in.arcadelabs.lifesteal.database.querybuilder.UpdateQuery;
import in.arcadelabs.lifesteal.utils.LifeState;
import lombok.AccessLevel;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileManager {

  @Getter(AccessLevel.NONE)
  private final DatabaseHandler databaseHandler =
          LifeStealPlugin.getLifeSteal().getDatabaseHandler();

  private final Map<UUID, Profile> profileMap = new HashMap<>();

  public ProfileManager() {

    String query =
            new CreateTableQuery("lifesteal_data")
                    .ifNotExists()
                    .column("uniqueID", "VARCHAR(36)")
                    .column("lifeState", "VARCHAR(20)")
                    .column("currentHearts", "INT(255)")
                    .column("lostHearts", "INT(255)")
                    .column("blessedHearts", "INT(255)")
                    .column("normalHearts", "INT(255)")
                    .column("cursedHearts", "INT(255)")
                    .column("peakHeartsReached", "INT(255)")
                    .primaryKey("uniqueID")
                    .build();

    System.out.println(query);

    try (Connection connection = databaseHandler.getConnection()) {
      Statement statement = connection.createStatement();
      statement.execute(query);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public boolean hasProfile(UUID uuid) {
    String sql = new SelectQuery("lifesteal_data").column("*").where("uniqueID = ?").build();

    System.out.println(sql);

    try (Connection connection = databaseHandler.getConnection()) {
      Query query = new Query(connection, sql);
      query.setParameter(1, uuid.toString());
      ResultSet resultSet = query.getStatement().executeQuery();
      return resultSet.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public Profile getProfile(UUID uuid) throws SQLException {
    Profile profile = new Profile(uuid);

    if (this.hasProfile(uuid)) {
      String sql = new SelectQuery("lifesteal_data").column("*").where("uniqueID = ?").build();

      System.out.println(sql);

      try (Connection connection = databaseHandler.getConnection()) {

        Query query = new Query(connection, sql);
        query.setParameter(1, uuid.toString());
        ResultSet resultSet = query.getStatement().executeQuery();

        profile.setLifeState(LifeState.valueOf(resultSet.getString("lifeState")));
        profile.setCurrentHearts(resultSet.getInt("currentHearts"));
        profile.setLostHearts(resultSet.getInt("lostHearts"));
        profile.setBlessedHearts(resultSet.getInt("blessedHearts"));
        profile.setNormalHearts(resultSet.getInt("normalHearts"));
        profile.setCursedHearts(resultSet.getInt("cursedHearts"));
        profile.setPeakHeartsReached(resultSet.getInt("peakHeartsReached"));

      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      this.saveProfile(profile);
    }

    return profile;
  }

  public Profile saveProfile(Profile profile) throws SQLException {
    if (hasProfile(profile.getUniqueID())) {
      String sql =
              new UpdateQuery("lifesteal_data")
                      .set("lifeState", profile.getLifeState().toString())
                      .where("uniqueID = ?")
                      .build();

      System.out.println(sql);

      try (Connection connection = databaseHandler.getConnection()) {

        Query query = new Query(connection, sql);
        query.setParameter(1, profile.getUniqueID().toString());
        query.getStatement().executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      String sql = new InsertQuery("lifesteal_data")
              .value("uniqueID")
              .value("lifeState")
              .value("currentHearts")
              .value("lostHearts")
              .value("blessedHearts")
              .value("normalHearts")
              .value("cursedHearts")
              .value("peakHeartsReached")
              .build();

      System.out.println(sql);
      try (Connection connection = databaseHandler.getConnection()) {
        Query query = new Query(connection, sql);
        query.getStatement().setString(1, profile.getUniqueID().toString());
        query.getStatement().setString(2, LifeState.LIVING.toString());
        query.getStatement().setString(3, String.valueOf(LifeStealPlugin.getLifeSteal().getConfig().getInt("DefaultHealth", 20)));
        query.getStatement().setString(4, "0");
        query.getStatement().setString(5, "0");
        query.getStatement().setString(6, "0");
        query.getStatement().setString(7, "0");
        query.getStatement().setString(8, String.valueOf(LifeStealPlugin.getLifeSteal().getConfig().getInt("DefaultHealth", 20)));
        query.getStatement().executeUpdate();
      }
    }
    return profile;
  }

  public void saveAll() throws SQLException {
    for (Profile profile : profileMap.values()) {
      this.saveProfile(profile);
    }
  }
}
