class AddRatingToRoom < ActiveRecord::Migration[5.0]
  def change
    add_column :rooms, :rating, :float
  end
end
