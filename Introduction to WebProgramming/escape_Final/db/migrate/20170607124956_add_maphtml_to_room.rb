class AddMaphtmlToRoom < ActiveRecord::Migration[5.0]
  def change
    add_column :rooms, :maphtml, :text
  end
end
