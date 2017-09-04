json.extract! reply, :id, :usr, :comment, :int, :rating, :created_at, :updated_at
json.url reply_url(reply, format: :json)
